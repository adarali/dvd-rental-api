package com.example.rest.dvdrental.v2.controllers;

import com.example.rest.dvdrental.v2.config.security.AppUserDetails;
import com.example.rest.dvdrental.v2.config.security.AppUserDetailsService;
import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.model.AuthRequest;
import com.example.rest.dvdrental.v2.model.PasswordRequest;
import com.example.rest.dvdrental.v2.model.VerificationRequest;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.service.EmailService;
import com.example.rest.dvdrental.v2.service.PasswordRecoveryTokenService;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("user")
public class UserController {
    
    private final AppUserDetailsService userDetailsService;
    private final AppUserService appUserService;
    private final EmailService emailService;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final JwtUtil jwtUtil;
    
    public UserController(
            AppUserDetailsService userDetailsService,
            AppUserService appUserService,
            EmailService emailService,
            PasswordRecoveryTokenService passwordRecoveryTokenService,
            JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.appUserService = appUserService;
        this.emailService = emailService;
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("verify")
    public String verify(@RequestParam String code, Model model) {
        VerificationRequest authRequest = new VerificationRequest();
        authRequest.setCode(code);
        model.addAttribute("authRequest", authRequest);
        return "user/verification";
    }
    
    @PostMapping("verify")
    public String postVerify(VerificationRequest authRequest, RedirectAttributes ra) {
        try {
            AppUserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            if (userDetailsService.verifyCredentials(new AuthRequest(authRequest.getUsername(), authRequest.getPassword()), userDetails)) {
                AppUser user = userDetails.getUser();
                if (user.getVerificationCode().equals(authRequest.getCode())) {
                    appUserService.setUserVerified(user.getUsername());
                    return "user/verificationSuccessful";
                } else {
                    throw new AppException("Invalid verification code");
                }
            } else {
                throw new UsernameNotFoundException("");
            }
        } catch (UsernameNotFoundException e) {
            ra.addFlashAttribute("message", "Incorrect username or password");
        } catch (AppException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/user/verify?code="+authRequest.getCode();
    }
    
    @GetMapping("recovery/{username}/{token}")
    private String recover(@PathVariable String username, @PathVariable String token, Model model) {
        PasswordRequest recovery = new PasswordRequest();
        model.addAttribute("recovery", recovery);
        return "user/recovery";
    }
    
    @PostMapping("recovery/{username}/{token}")
    private String recover(
            PasswordRequest request,
            @PathVariable String username,
            @PathVariable String token, Model model,
            RedirectAttributes ra
    ) {
        try {
            AppUser user = appUserService.findByUsername(username);
            jwtUtil.validatePasswordToken(token, jwtUtil.getJwtDecoder(jwtUtil.generatePasswordSecretKey(user)));
            if (request.getPassword() == null) {
                throw new AppException("The password is required");
            }
            
            if (!request.match()) {
                throw new AppException("The passwords don't match");
            }
            
            appUserService.changePassword(user, request.getPassword());
    
            return "user/recoverySuccessful";
            
        } catch (AppException | UsernameNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return String.format("redirect:/user/recovery/%s/%s", username, token);
    }
    
    
    
}
