package com.example.rest.dvdrental.v2.config;

import com.example.rest.dvdrental.v2.entities.*;
import com.example.rest.dvdrental.v2.enums.ChangeType;
import com.example.rest.dvdrental.v2.service.*;
import com.example.rest.dvdrental.v2.utils.AppUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * The purpose of this class is to insert data into the database for testing
 */
@Component
@Log
public class DataInitializer {
    
    @Value("${app.initdata:false}") private boolean initData;
    
    private final MovieService movieService;
    private final AppUserService appUserService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final ChangeLogService changeLogService;
    private final RentService rentService;
    private final PurchaseService purchaseService;
    
    public DataInitializer(MovieService movieService, AppUserService appUserService, ObjectMapper objectMapper, PasswordEncoder passwordEncoder, ChangeLogService changeLogService, RentService rentService, PurchaseService purchaseService) {
        this.movieService = movieService;
        this.appUserService = appUserService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.changeLogService = changeLogService;
        this.rentService = rentService;
        this.purchaseService = purchaseService;
    }
    
    
    @Bean
    public CommandLineRunner init() {
        return args -> {
            addAdmin();
            if (initData) {
                addData();
            }
        };
    }
    
    private void addData() {
        log.info("Adding test data");
        addUsers();
        addMovies();
//        addChangeLogs();
//        addRents();
//        addPurchases();
        log.info("Data added successfully");
    }
    
    private void addMovies() {
        log.info("Adding movies");
        Map<String, Object> movieMap = new HashMap<>();
    
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = getClass().getClassLoader().getResource("movies200.json");
            MovieListWrapper wrapper = mapper.readValue(url, MovieListWrapper.class);
            List<Movie> movieList = wrapper.getMovies();
            for (Movie movie : movieList) {
                if("null".equals(movie.getTitle())) continue;
                movie.setStock(BigDecimal.valueOf(100));
                movie.setRentalPrice(AppUtils.getRandomBigDecimal(1, 101));
                movie.setSalePrice(movie.getRentalPrice().multiply(BigDecimal.valueOf(5)));
                Collections.reverse(movie.getMovieImages());
                movieService.create(movie);
            }
            log.info("Movies added successfully");
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    private void addAdmin() {
        if(appUserService.countAdmin() > 0) return;
        log.info("Adding admin");
        
        AppUser admin = new AppUser();
        admin.setAdmin(true);
        admin.setUsername("admin1");
        admin.setEmail("admin1@gmail.com");
        String password = "12345";
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFirstName("Admin");
        admin.setLastName("Juan");
        admin.setPicture("https://pickaface.net/gallery/avatar/unr_admin_171016_2225_zewhi.png");
        appUserService.create(admin);
        
        log.info("Admin has been added successfully");
        log.info("Admin's username: " + admin.getUsername());
        log.info("Admin's password: " + password);
    }
    
    private void addUsers() {
        log.info("Adding users");
        
        
        
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setFirstName("User");
        user.setLastName("Juan");
        user.setEmail("someemail115@gmail.com");
        user.setPassword(passwordEncoder.encode("12345"));
    
        appUserService.create(user);
        
        AppUser user2 = new AppUser();
        user2.setUsername("user2");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setEmail("mj@hotmail.com");
        user2.setVerified(true);
        user2.setPassword(passwordEncoder.encode("12345"));
    
        appUserService.create(user2);
        
        log.info("Users added successfully");
    }
    
    private void addChangeLogs() {
        log.info("Adding changelogs");
        Map<Long, Movie> movieMap = movieService.findAll().stream().collect(Collectors.toMap(Movie::getId, Function.identity()));
    
        for (int i = 0; i < 200; i++) {
            ChangeLog changeLog1 = new ChangeLog();
            changeLog1.setMovie(movieMap.get(RandomUtils.nextLong(1, 501)));
            LocalDateTime time = LocalDateTime.now().minusHours(RandomUtils.nextInt(0, 24));
            changeLog1.setChangedDate(time.minusMonths(RandomUtils.nextInt(0, 12)));
            changeLog1.setChangeType(ChangeType.values()[RandomUtils.nextInt(0,3)]);
            changeLog1.setOldValue(String.format("Old value %03d", i + 1));
            changeLog1.setNewValue(String.format("New value %03d", i + 1));
    
            changeLogService.create(changeLog1);
        }
    }
    
    private void addRents() {
        log.info("Adding rents");
        
        List<Movie> movies = movieService.findAll();
        List<AppUser> users = appUserService.findAll();
        users.removeIf(user -> user.isAdmin());
        for (int i = 1; i <= 100; i++) {
            Rent rent = new Rent();
            rent.setMovie(movies.get(RandomUtils.nextInt(0, movies.size())));
            rent.setUser(users.get(RandomUtils.nextInt(0, users.size())));
            rent.setRentDate(LocalDate.now().minusDays(RandomUtils.nextInt(0, 100)));
            rent.setExpectedReturnDate(rent.getRentDate().plusDays(RandomUtils.nextInt(1, 10)));
            rent.setActualReturnDate(RandomUtils.nextBoolean() ? rent.getRentDate().plusDays(RandomUtils.nextInt(1, 20)) : null);
            rent.setQuantity(AppUtils.getRandomBigDecimal(1,11));
            rentService.create(rent);
        }
    }
    
    private void addPurchases() {
        log.info("Adding purchases");
        
        List<Movie> movies = movieService.findAll();
        List<AppUser> users = appUserService.findAll();
        users.removeIf(user -> user.isAdmin());
        for (int i = 1; i <= 100; i++) {
            Purchase purchase = new Purchase();
            purchase.setMovie(movies.get(RandomUtils.nextInt(0, movies.size())));
            purchase.setUser(users.get(RandomUtils.nextInt(0, users.size())));
            LocalDateTime time = LocalDateTime.now().minusHours(RandomUtils.nextInt(0, 24));
            purchase.setPurchaseDateTime(time.minusMonths(RandomUtils.nextInt(0, 12)));
            purchase.setQuantity(AppUtils.getRandomBigDecimal(1,26));
            purchaseService.create(purchase);
        }
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    private static class RandomUser {
        private String gender;
        private RandomUserName name;
        private String email;
        private RandomUserPicture picture;
        
        public Movie toMovie() {
            Movie movie = new Movie();
            movie.setTitle(this.name.toString());
            movie.setDescription(String.format("This is a movie about %s", movie.getTitle()));
            movie.setRentalPrice(AppUtils.getRandomBigDecimal(1, 100).setScale(2));
            movie.setSalePrice(AppUtils.getRandomBigDecimal(1, 100).setScale(2));
            movie.setStock(new BigDecimal("100.00"));
            movie.getMovieImages().add(new MovieImage(movie, picture.getLarge()));
            movie.getMovieImages().add(new MovieImage(movie, picture.getMedium()));
            movie.getMovieImages().add(new MovieImage(movie, picture.getThumbnail()));
//            movie.setLikeCount(RandomUtils.nextInt(0,100));
            return movie;
        }
    }
    
    @Getter
    @Setter
    private static class RandomUserPicture {
        private String large;
        private String medium;
        private String thumbnail;
    }
    
    @Getter
    @Setter
    private static class Results{
        List<RandomUser> results = new ArrayList<>();
    
        public Results() {
        }
    }
    
    @Getter
    @Setter
    private static class RandomUserName {
        private String first;
        private String last;
    
        @Override
        public String toString() {
            return String.format("%s %s", first, last);
        }
    }
    
    
    @Getter
    @Setter
    private static class MovieListWrapper {
        List<Movie> movies = new ArrayList<>();
    }

}
