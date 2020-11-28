package com.example.rest.dvdrental.v2.controllers;

import com.example.rest.dvdrental.v2.entities.AppUser;
import com.example.rest.dvdrental.v2.exceptions.AppException;
import com.example.rest.dvdrental.v2.model.ChangeRoleRequest;
import com.example.rest.dvdrental.v2.model.UserResponse;
import com.example.rest.dvdrental.v2.service.AppUserService;
import com.example.rest.dvdrental.v2.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    
    private final AppUserService appUserService;
    private final EmailService emailService;
    
    public UserRestController(AppUserService appUserService, EmailService emailService) {
        this.appUserService = appUserService;
        this.emailService = emailService;
    }
    
    @Operation(summary = "Send recovery email (Requires admin privileges)"
            , description = "Send a recovery email to a user by providing his username to rest the password")
    @ApiResponse(responseCode = "200")
    @PostMapping("{username}/recovery-email")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> sendRecoveryEmail(@PathVariable @Parameter(in = ParameterIn.PATH, description = "The username of the user") String username) {
        AppUser user = appUserService.findByUsername(username);
        appUserService.sendRecoveryEmail(user);
        return ResponseEntity.ok(String.format("Recovery email has been sent successfully to %s", user.getEmail()));
    }
    
    @Operation(summary = "Creates a new user (Requires admin privileges)"
            , description = "Adds a new user with the provided username to the database"
            , requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(example = "{\"username\":\"adamsandler\",\"firstName\":\"Adam\",\"lastName\":\"Sandler\",\"fullName\":\"Adam Sandler\",\"email\":\"adamsandler@gmail.com\",\"admin\":false,\"enabled\":true,\"verified\":true}")))
    )
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "409", description = "If another user in the database has the same username")
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponse> addUser(@RequestBody Map<String, Object> userRequest) {
        UserResponse response = new UserResponse(appUserService.saveUser(userRequest, true));
        Link link = linkTo(methodOn(getClass()).userInfo(response.getUsername())).withSelfRel();
        return ResponseEntity.created(link.toUri()).body(response);
    }
    
    @Operation(summary = "Modifies a new user (Requires admin privileges)"
            , description = "If the username provided exists in the database, this operation modifies the user"
            , requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(example = "{\"username\":\"adamsandler\",\"firstName\":\"Adam\",\"lastName\":\"Sandler\",\"fullName\":\"Adam Sandler\",\"email\":\"adamsandler@gmail.com\",\"admin\":false,\"enabled\":true,\"verified\":true}")))
    )
    @ApiResponse(responseCode = "200")
    @PutMapping("{username}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponse> updateUser(@RequestBody Map<String, Object> userRequest) {
        return ResponseEntity.ok(new UserResponse(appUserService.saveUser(userRequest, false)));
    }
    
    @Operation(summary = "Obtains user information (Requires admin privileges)"
            , description = "This operation obtains the information of a user by providing his username"
            , parameters = @Parameter(in = ParameterIn.PATH, description = "The username of the user to get the information of"))
    @ApiResponse(responseCode = "200")
    @GetMapping("{username}")
    @Secured("ROLE_ADMIN")
    public UserResponse userInfo(@PathVariable String username) {
        return new UserResponse(appUserService.findByUsername(username));
    }
    
    @Operation(summary = "Deletes a user (Requires admin privileges)"
            , description = "This operation deletes a user from the database by providing his username"
            , parameters = @Parameter(in = ParameterIn.PATH, description = "The username of the user to delete"))
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{username}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        appUserService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Send verification email (Requires admin privileges)"
            , description = "This operation sends an email to a user so his email can be verified")
    @ApiResponse(responseCode = "200")
    @GetMapping("{username}/email-verification")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> sendVerificationEmail(@PathVariable @Parameter(in = ParameterIn.PATH, description = "The username of the user") String username) {
        AppUser user = appUserService.findByUsername(username);
        emailService.sendVerificationEmail(user);
        return ResponseEntity.ok("Email was sent successfully to " + user.getEmail());
    }
    
    @Operation(summary = "Change user's role (Requires admin privileges)"
            , description = "This operation changes the role of the user by providing his username")
    @ApiResponse(responseCode = "200")
    @PatchMapping("{username}/role")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> changeRole(@PathVariable("username") String username, @RequestBody ChangeRoleRequest request, Principal principal) {
        if (principal.getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You cannot change your own role");
        }
        boolean admin = request.isAdmin();
        appUserService.changeRole(username, admin);
        if (admin) {
            return ResponseEntity.ok(String.format("User %s is now an admin", username));
        }
        return ResponseEntity.ok(String.format("User %s is not an admin anymore", username));
    }
}
