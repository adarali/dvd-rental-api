package com.example.rest.dvdrental.v2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
public class Beans {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8086");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Link");
        config.addExposedHeader("X-Total-Count");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
    @Bean
    public OpenAPI publicApi(@Value("${app.baseUrl}") String baseUrl) {
        return new OpenAPI().info(apiDetails())
//                .addServersItem(new Server().url("https://limitless-temple-47840.herokuapp.com").description("Heroku Test Server"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local Server"));
    }
    
    
    private Info apiDetails() {
        
        Info info = new Info();
        Contact contact = new Contact();
        contact.setName("Ask Me");
        contact.setEmail("askme297@gmail.com");
        
        info.setContact(contact);
        info.setTitle("DVD Rental API");
        info.setDescription("This is a DVD Rental API. To make requests that require authentication with the API, you need to provide a JWT Token in the headers under the key name \"jwt\" which you obtain after being authenticated.");
        info.setVersion("1.0.0");
        info.setLicense(new License());
        return info;
        
    }
    
    
}
