package com.example.rest.dvdrental.v2.config.security;

import com.example.rest.dvdrental.v2.utils.AppJwtAuthenticationConverter;
import com.example.rest.dvdrental.v2.utils.AppUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final AppAuthenticationSuccessHandler authenticationSuccessHandler;
    private final Environment environment;
    
    public WebSecurityConfig(AppAuthenticationSuccessHandler authenticationSuccessHandler, Environment environment) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.environment = environment;
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(AppUtils.getPermitAll().toArray(new String[]{})).permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/movies/**").permitAll()
                .anyRequest().authenticated();
        
        http.oauth2Login().successHandler(authenticationSuccessHandler);
        http.exceptionHandling().defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"));
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new AppJwtAuthenticationConverter());
        
        //to be able to view h2 console
        if(Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            http.headers().frameOptions().disable();
        }
        
    }
}
