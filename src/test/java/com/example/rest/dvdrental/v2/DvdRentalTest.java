package com.example.rest.dvdrental.v2;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.entities.Movie;
import com.example.rest.dvdrental.v2.entities.MovieImage;
import com.example.rest.dvdrental.v2.entities.Rent;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.exceptions.AppValidationException;
import com.example.rest.dvdrental.v2.exceptions.InvalidJwtTokenException;
import com.example.rest.dvdrental.v2.model.*;
import com.example.rest.dvdrental.v2.model.movie.MovieDetails;
import com.example.rest.dvdrental.v2.model.movie.MovieListItem;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.service.AuthTokenService;
import com.example.rest.dvdrental.v2.service.MovieService;
import com.example.rest.dvdrental.v2.service.RentService;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//@ActiveProfiles(profiles = "localdb")
public class DvdRentalTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RentService rentService;
    
    private String getBaseUrl() {
        return String.format("http://localhost:%d", port);
    }
    
    private String getUrl(String path) {
        return getBaseUrl() + path;
    }
    
    private ResponseEntity<AuthResponse> getAuthResponseEntity(String username) {
        return restTemplate.postForEntity(getUrl("/login/auth"), new AuthRequest("admin1", "12345"), AuthResponse.class);
    }
    
    private String getToken(String username) {
        return getAuthResponseEntity(username).getBody().getJwt();
    }
    
    private String token;
    
    @BeforeEach
    void setHeaders() {
//        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2MDQ5NTgyNzgsImV4cCI6MTYwNDk2MTg3OH0.p2o3UWyWf9MGVQKW5OJm-cTNsJM8NbCF89BFiXv3PBY";
        if (token == null) {
            this.token = getToken("admin");
        }
        String jwt = this.token;
        
//        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2MDQ5NzMxNjIsImV4cCI6MTYwNDk3Njc2Mn0.lYYupcg_pkMJXjkm6Ro7YhdGS64yqJWPGhNcD5prGP4";
        
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", "Bearer " + jwt);
                    return execution.execute(request, body);
                
                }));
    }
    
    /*@Test
    void testAuth() {
        ResponseEntity<String> entity = restTemplate.getForEntity(getUrl("/auth/test"), String.class);
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }*/
    
    @Test
    void testFindMovieById() {
        val entity = restTemplate.getForEntity(getUrl("/api/v1/movies/3"), MovieDetails.class);
        assertEquals(3, entity.getBody().getId());
    }
    
    @Test
    void testMovieNotFound() {
        val entity = restTemplate.getForEntity(getUrl("/api/v1/movies/7000"), MovieDetails.class);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }
    
    @Test
    void testGetUserInfo() {
        val entity = restTemplate.getForEntity(getUrl("/api/v1/users/admin1"), UserResponse.class);
        assertEquals("admin1", entity.getBody().getUsername());
    }
    
    @Test
    void testUserNotFound() {
        val entity = restTemplate.getForEntity(getUrl("/api/v1/users/dkok1"), UserResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }
    
    @Test
    void testAddMovie() {
        Movie movie = new Movie();
        movie.setTitle("A new title "+ RandomStringUtils.randomAlphanumeric(10));
        movie.setSalePrice(new BigDecimal("10.00"));
        movie.setRentalPrice(BigDecimal.ONE);
        movie.setStock(new BigDecimal("200"));
        movie.setAvailable(false);
        movie.setDescription("This is a test movie");
        movie.getMovieImages().add(new MovieImage(movie, "https://www.google.com/image.jpg"));
    
        val entity = restTemplate.postForEntity(getUrl("/api/v1/movies"), movie, Movie.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    }
    
    private Map<String, Object> getMovieMap() {
        Map<String, Object> map = new HashMap<>();
        String newDesc = "This movie description has been updated for testing";
        map.put("id", 1L);
        map.put("available", false);
        map.put("description", newDesc);
        return map;
    }
    
    @Test
    void testUpdateMovie() {
        val map = getMovieMap();
        String url = getUrl("/api/v1/movies/"+getMovieMap().get("id"));
        val entity = RequestEntity.put(URI.create(url)).body(map);
        val response = restTemplate.exchange(entity, Movie.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testUpdateMovieNoImages() {
        Map<String, Object> map = getMovieMap();
        URI uri = URI.create(getUrl(String.format("/api/v1/movies/%d", map.get("id"))));
        List<MovieImage> movieImageList = new ArrayList<>();
        map.put("movieImages", movieImageList);
        val entity = RequestEntity.put(uri).body(map);
        val response = restTemplate.exchange(entity, String.class);
        assertTrue(response.getBody().contains("The movie must have at least one image"));
    }
    
    @Test
    @Transactional
    void testAddMovieImage()  {
        Movie movie = movieService.getMovie(3L);
        movie.getMovieImages().add(new MovieImage(movie, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcT-psLEAjrzDm42EoPjQAohP3RulNx7myCrmw&usqp=CAU"));
        URI uri = URI.create(getUrl("/api/v1/movies/3"));
        val entity = RequestEntity.put(uri).body(movie);
        val response = restTemplate.exchange(entity, Movie.class);
        assertEquals(3, response.getBody().getMovieImages().size());
    }
    
    @Test
    @Transactional
    void testAddAndRemoveMovieImage() {
        Movie movie = movieService.getMovie(20L);
        movie.getMovieImages().remove(0);
        String imgUrl = "https://upload.wikimedia.org/wikipedia/commons/a/a7/Whitney_Houston_Welcome_Home_Heroes_1_cropped.jpg";
        movie.getMovieImages().add(new MovieImage(movie, imgUrl));
//        movieService.update(movie);
        URI uri = URI.create(getUrl("/api/v1/movies/"+movie.getId()));
        val entity = RequestEntity.put(uri).body(movie);
        val response = restTemplate.exchange(entity, Movie.class);
    
        Movie resp = response.getBody();
        if (resp.getMovieImages().size() != 2) {
            fail(String.format("The movie image list size should be 2 but it's %d instead", resp.getMovieImages().size()));
        }
        
        assertTrue(movie.getMovieImages().stream().anyMatch(mi -> StringUtils.equals(imgUrl, mi.getUrl())));
    }
    
    
    
    @Test
    void testFindMoviesWithpagination() throws URISyntaxException {
        URIBuilder builder = new URIBuilder("/api/v1/movies");
        builder.addParameter("page", "0");
        builder.addParameter("per_page", "10");
        builder.addParameter("sort", "title");
        builder.addParameter("title", "das");
        builder.addParameter("available", "1");
        
        val ref = new ParameterizedTypeReference<List<MovieListItem>>(){};
        
        val entity = RequestEntity.get(builder.build()).build();
    
        val response = restTemplate.exchange(entity, ref);
        assertEquals(3, response.getBody().size());
        
    }
    
    @Test
    void testDeleteMovie() {
        String url = getUrl("/api/v1/movies/6");
        val entity = RequestEntity.delete(URI.create(url)).build();
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    void testDeleteMovieNotFound() {
        String url = getUrl("/api/v1/movies/5468");
        val entity = RequestEntity.delete(URI.create(url)).build();
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    void testAvailability() {
        URI url = URI.create(getUrl("/api/v1/movies/5/available"));
        val entity = RequestEntity.patch(url).build();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testLike() {
        URI url = URI.create(getUrl("/api/v1/movies/50/like"));
        val entity = RequestEntity.patch(url).build();
        val response = restTemplate.exchange(entity, LikeResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testChangeLog() throws URISyntaxException {
        ChangeLogRequest request = new ChangeLogRequest();
        request.setDateFrom(LocalDate.now().minusDays(10));
        request.setDateTo(LocalDate.now());
        URIBuilder builder = new URIBuilder(getUrl("/api/v1/changes"));
        builder.addParameter("dateFrom", request.getDateFrom().format(getDateFormatter()));
        builder.addParameter("dateTo", request.getDateTo().format(getDateFormatter()));
        val entity = RequestEntity.get(builder.build()).build();
        val response = restTemplate.exchange(entity, new ParameterizedTypeReference<List<ChangeLogResponse>>(){});
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testPurchase() {
        PurchaseRequest request = new PurchaseRequest(5L, 3);
        val entity = RequestEntity.post(URI.create(getUrl(String.format("/api/v1/movies/%d/purchases", request.getMovieId())))).body(request);
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
    
    @Test
    void testPurchaseLogs() throws URISyntaxException {
        PurchaseLogRequest request = new PurchaseLogRequest();
        request.setDateFrom(LocalDate.now().minusDays(10));
        request.setDateTo(LocalDate.now());
        URIBuilder builder = new URIBuilder(getUrl("/api/v1/purchases"));
        builder.addParameter("dateFrom", request.getDateFrom().format(getDateFormatter()));
        builder.addParameter("dateTo", request.getDateTo().format(getDateFormatter()));
        val entity = RequestEntity.get(builder.build()).build();
        val response = restTemplate.exchange(entity, new ParameterizedTypeReference<List<PurchaseLogResponse>>(){});
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testRent() {
        RentRequest request = new RentRequest(8L, 1, 2);
        val entity = RequestEntity.post(URI.create(getUrl("/api/v1/movies/8/rents"))).body(request);
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
    
    @Test
    void testRentLog() throws URISyntaxException {
        RentLogRequest request = new RentLogRequest();
        request.setDateFrom(LocalDate.now().minusDays(10));
        request.setDateTo(LocalDate.now());
        URIBuilder builder = new URIBuilder(getUrl("/api/v1/rents"));
        builder.addParameter("dateFrom", request.getDateFrom().format(getDateFormatter()));
        builder.addParameter("dateTo", request.getDateTo().format(getDateFormatter()));
        val entity = RequestEntity.get(builder.build()).build();
        val response = restTemplate.exchange(entity, new ParameterizedTypeReference<List<PurchaseLogResponse>>() {});
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
//    @Transactional
    void returnRent() {
        Rent rent = rentService.rent(new RentRequest(67L, 3, 1), "admin1");
        val entity = RequestEntity.patch(URI.create(getUrl(String.format("/api/v1/movies/67/rents/%s/return", rent.getId())))).build();
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
    
    @Test
    void testDeleteUser() {
        val entity = RequestEntity.delete(URI.create("/api/v1/users/user1")).build();
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(204, response.getStatusCodeValue());
    }
    
    @Test
    void testSendRecoveryEmail() {
        val entity = RequestEntity.post(URI.create(getUrl("/api/v1/users/user2/recovery-email"))).build();
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
    
    @Test
    void testSendVerificationEmail() {
        String url = getUrl("/api/v1/users/user1/email-verification");
        val entity = RequestEntity.get(URI.create(url)).build();
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
    
    @Test
    void testCreateUser() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "sam123");
        map.put("password", "12345");
        map.put("email", "sam123@gmail.com");
        map.put("firstName", "Sam");
        map.put("lastName", "Harris");
        val entity = RequestEntity.post(URI.create(getUrl("/api/v1/users"))).body(map);
        val response = restTemplate.exchange(entity, UserResponse.class);
        assertEquals("Sam Harris", response.getBody().getFullName());
    }
    
    @Test
    void testModifyUser() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "user1");
        map.put("firstName", "Sammy");
        val entity = RequestEntity.put(URI.create(getUrl("/api/v1/users/user1"))).body(map);
        val response = restTemplate.exchange(entity, UserResponse.class);
        assertEquals("Sammy", response.getBody().getFirstName());
    }
    
    @Test
    void testChangeRole() {
        URI uri = URI.create(getUrl("/api/v1/users/user2/role"));
        ChangeRoleRequest request = new ChangeRoleRequest();
        request.setUsername("user2");
        request.setAdmin(true);
        val entity = RequestEntity.patch(uri).body(request);
        val response = restTemplate.exchange(entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testChangeOwnRole() {
        String resp = "You cannot change your own role";
        ChangeRoleRequest request = new ChangeRoleRequest();
        request.setUsername("admin1");
        request.setAdmin(true);
        URI uri = URI.create(getUrl("/api/v1/users/admin1/role"));
    
        val entity = RequestEntity.patch(uri).body(request);
        val response = restTemplate.exchange(entity, String.class);
        
        assertTrue(response.getBody().contains(resp));
    }
    
//    @Test
    void testLogout() {
        URI uri = URI.create(getUrl("/auth/logout"));
        restTemplate.exchange(RequestEntity.get(uri).build(), String.class);
        assertThrows(InvalidJwtTokenException.class, () -> authTokenService.validateToken(this.token));
    }
    
    @Test
    void testChangePassword() {
        String password = RandomStringUtils.randomAlphanumeric(20);
        AppUser user = appUserService.findByUsername("user1");
        appUserService.changePassword(user, password);
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }
    
    @Test
    void createUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("alpha");
        userRequest.setPassword("12345");
        userRequest.setAdmin(true);
        userRequest.setEmail("mj@hotmail.com");
        userRequest.setFirstName("Adam");
        userRequest.setLastName("Adam");
        appUserService.saveUser(userRequest);
        
    }
    
    @Test
    void testSetUserVerified() {
        String username = "user1";
        appUserService.setUserVerified(username);
        AppUser user = appUserService.findByUsername(username);
        assertTrue(user.isVerified());
    }
    
    @Test
    void testRecoveryEmailFail() {
        AppUser user = appUserService.findByUsername("user1");
        assertThrows(AppException.class, () -> appUserService.sendRecoveryEmail(user));
    }
    
    @Test
    void testRentFail() {
        RentRequest request = new RentRequest();
        request.setQuantity(0);
        request.setRentDays(0);
        
        try {
            rentService.rent(request, "user1");
            fail();
        } catch (AppValidationException e){
            assertTrue(e.getMessages().size() == 3);
        }
    }
    
    @Test
    void testRentNotExists() {
         try {
             rentService.getRent(3432L);
             fail("Should have thrown an exception");
         } catch (AppException e) {
             assertEquals("The rent does not exist", e.getMessage());
         }
     }
     
     private DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
     }
    
}
