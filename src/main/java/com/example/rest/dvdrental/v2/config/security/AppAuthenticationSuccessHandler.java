package com.example.rest.dvdrental.v2.config.security;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
@Log
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtUtil jwtUtil;
    private final AppUserService appUserService;
    
    public AppAuthenticationSuccessHandler(JwtUtil jwtUtil, AppUserService appUserService) {
        this.jwtUtil = jwtUtil;
        this.appUserService = appUserService;
    }
    
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) token.getPrincipal();
        String userId = token.getPrincipal().getName();
        String registrationId = token.getAuthorizedClientRegistrationId();
        String username = String.format("%s_%s", registrationId, userId);
        log.info("Username: " + username);
        String jwt = jwtUtil.generateToken(new AppUser(username), 1);
        log.info("User attributes: ");
        log.info(Arrays.toString(oauth2User.getAttributes().entrySet().toArray()));
        appUserService.saveUser(oauth2User, registrationId);
        String referer = request.getHeader("referer");
        setUseReferer(true);
        setTargetUrlParameter(jwt);
        response.addCookie(new Cookie("token", jwt));
        if(referer != null) {
            return String.format("%s?token=%s", referer.split("\\?")[0], jwt);
        }
        return super.determineTargetUrl(request, response);
    }
}
