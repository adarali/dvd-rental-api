package com.example.rest.dvdrental.v2.controllers;

import com.smbssolutions.test.dvd_rental.entities.AppUser;
import com.smbssolutions.test.dvd_rental.exceptions.AppException;
import com.smbssolutions.test.dvd_rental.model.ChangeRoleRequest;
import com.smbssolutions.test.dvd_rental.model.UserResponse;
import com.smbssolutions.test.dvd_rental.service.AppUserService;
import com.smbssolutions.test.dvd_rental.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserAdminController {
    
    private final AppUserService appUserService;
    private final EmailService emailService;
    
    public UserAdminController(AppUserService appUserService, EmailService emailService) {
        this.appUserService = appUserService;
        this.emailService = emailService;
    }
    
    @Operation(summary = "Send recovery email (Requires admin privileges)"
            , description = "Send a recovery email to a user by providing his username to rest the password"
            , parameters = @Parameter(in = ParameterIn.PATH, description = "The username of the user"))
    @ApiResponse(responseCode = "200")
    @GetMapping("admin/recovery-email/{username}")
    public ResponseEntity<?> sendRecoveryEmail(@PathVariable String username) {
        AppUser user = appUserService.findByUsername(username);
        appUserService.sendRecoveryEmail(user);
        return ResponseEntity.ok(String.format("Recovery email was sent successfully to %s", user.getEmail()));
    }
    
    @Operation(summary = "Creates a new user or modifies an existing one (Requires admin privileges)"
            , description = "If the username provided exists in the database, this operation modifies that user. Otherwise, it creates a new one"
            , requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(example = "{\"username\":\"adamsandler\",\"firstName\":\"Adam\",\"lastName\":\"Sandler\",\"fullName\":\"Adam Sandler\",\"email\":\"adamsandler@gmail.com\",\"admin\":false,\"enabled\":true,\"verified\":true}")))
    )
    @ApiResponse(responseCode = "200")
    @PostMapping("admin/save")
    public UserResponse saveUser(@RequestBody Map<String, Object> userRequest) {
        return new UserResponse(appUserService.saveUser(userRequest));
        
    }
    
    @Operation(summary = "Obtains user information (Requires admin privileges)"
            , description = "This operation obtains the information of a user by providing his username"
            , parameters = @Parameter(in = ParameterIn.PATH, description = "The username of the user to get the information of"))
    @ApiResponse(responseCode = "200")
    @GetMapping("admin/info/{username}")
    private UserResponse userInfo(@PathVariable String username) {
        return new UserResponse(appUserService.findByUsername(username));
    }
    
    @Operation(summary = "Deletes a user (Requires admin privileges)"
            , description = "This operation deletes a user from the database by providing his username"
            , parameters = @Parameter(in = ParameterIn.PATH, description = "The username of the user to delete"))
    @ApiResponse(responseCode = "200")
    @DeleteMapping("admin/{username}")
    private ResponseEntity<?> delete(@PathVariable String username){
        appUserService.deleteByUsername(username);
        return ResponseEntity.ok(String.format("the user %s was deleted successfully", username));
    }
    
    @Operation(summary = "Send verification email (Requires admin privileges)"
            , description = "This operation sends an email to a user so his email can be verified"
            , parameters = @Parameter(in = ParameterIn.PATH, description = "The username of the user"))
    @ApiResponse(responseCode = "200")
    @GetMapping("admin/email-verification/{username}")
    public ResponseEntity<?> sendVerificationEmail(@PathVariable String username) {
        AppUser user = appUserService.findByUsername(username);
        emailService.sendVerificationEmail(user);
        return ResponseEntity.ok("Email was sent successfully to " + user.getEmail());
    }
    
    @Operation(summary = "Change the role of a user (Requires admin privileges)"
            , description = "This operation changes the role of the user by providing his username")
    @ApiResponse(responseCode = "200")
    @PostMapping("admin/role-change")
    public ResponseEntity<?> changeRole(@RequestBody ChangeRoleRequest request, Principal principal) {
        String username = request.getUsername();
        if (principal.getName().equals(username)) {
            throw new AppException("You cannot change your own role");
        }
        boolean admin = request.isAdmin();
        appUserService.changeRole(username, admin);
        if (admin) {
            return ResponseEntity.ok(String.format("User %s is now an admin", username));
        }
        return ResponseEntity.ok(String.format("User %s is not an admin anymore", username));
    }
}
