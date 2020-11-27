package com.example.rest.dvdrental.v2.utils;

import com.example.rest.dvdrental.v2.config.security.AppUserDetails;
import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.model.LazyRequest;
import com.example.rest.dvdrental.v2.model.LazyResponse;
import com.example.rest.dvdrental.v2.model.movie.MovieQuery;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.*;

public class AppUtils {
    
    public static BigDecimal getRandomBigDecimal(Integer from, Integer to) {
        return BigDecimal.valueOf(RandomUtils.nextInt(from, to));
    }
    
    public static AppUser getCurrentUser() {
        AppUser user = getCurrentUserOrNull();
        if(user != null) return user;
        throw new AppException("You are not authenticated");
    }
    
    public static AppUser getCurrentUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getPrincipal() instanceof AppUserDetails)) {
            return ((AppUserDetails) auth.getPrincipal()).getUser();
        }
        return null;
    }
    
    public static String generateVerificationCode() {
        return RandomStringUtils.randomAlphanumeric(20);
    }
    
    
    public static void main(String[] args) {
        System.out.println(generateVerificationCode());
    }
    
    public static List<String> getPermitAll() {
        return Arrays.asList(
                "/",
                "/login/**",
                "/h2-console/**",
                "/user/verify",
                "/user/recovery/**",
                "/resources/**",
                "/app/**",
                "/api-docs/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/error"
        );
    }
    
    public static String getToken(HttpServletRequest request) {
        String key = "Authorization";
        /*Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }*/
        String header = request.getHeader(key);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    
    public static <T> Map<String, Object> objectToMap(T object, List<String> fields, ObjectMapper mapper) {
        Map<String, Object> map = mapper.convertValue(object, Map.class);
        map.entrySet().removeIf(entry -> !fields.contains(entry.getKey()));
        return map;
    }
    
    public static String buildPaginationLinkHeaders(LazyRequest request, LazyResponse response, HttpServletRequest req) throws URISyntaxException {
        Map<String, String[]> params = new HashMap<>(req.getParameterMap());
        params.put("per_page", new String[]{String.valueOf(request.getPageSize())});
        StringJoiner joiner = new StringJoiner(", ");
        int lastPage = (int) (response.getTotalRecords() / request.getPageSize());
    
        if (request.getPage() < lastPage) {
            URIBuilder builder = new URIBuilder(req.getRequestURL().toString());
            params.put("page", new String[]{String.valueOf(request.getPage() + 1)});
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue()[0]);
            }
            joiner.add(String.format("<%s>; rel=\"next\"", builder.build()));
        }
    
        if (request.getPage() > 0) {
            URIBuilder prevBuilder = new URIBuilder(req.getRequestURL().toString());
            params.put("page", new String[]{String.valueOf(request.getPage() - 1)});
        
            for (String s : params.keySet()) {
                prevBuilder.addParameter(s, params.get(s)[0]);
            }
            joiner.add(String.format("<%s>; rel=\"prev\"", prevBuilder.build()));
        }
        
        URIBuilder lastBuilder = new URIBuilder(req.getRequestURL().toString());
        params.put("page", new String[]{String.valueOf(lastPage)});
        for (String key : params.keySet()) {
            lastBuilder.addParameter(key, params.get(key)[0]);
        }
        URIBuilder firstBuilder = new URIBuilder(req.getRequestURL().toString());
        params.put("page", new String[]{"0"});
        for (String key : params.keySet()) {
            firstBuilder.addParameter(key, params.get(key)[0]);
        }
        joiner.add(String.format("<%s>; rel=\"first\"", firstBuilder.build()));
        joiner.add(String.format("<%s>; rel=\"last\"", lastBuilder.build()));
        return joiner.toString();
    }
    
    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new String(Base64.getEncoder().encode(bytes));
    }
}
