package com.example.rest.dvdrental.v2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.rest.dvdrental.v2.utils.AppUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class AppUser extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "The username is required")
    private String username;
    @NotBlank(message = "The password")
    private String password;
    @Email(message = "The email is invalid")
//    @NotBlank(message = "The email is required")
    private String email;
    @NotBlank(message = "The role is required")
    private String role = "USER";
    @NotBlank(message = "The first name is required")
    private String firstName;
    private String lastName;
    @ManyToMany(mappedBy = "likes")
    @JsonIgnore
    private List<Movie> likes = new ArrayList<>();
    private boolean verified;
    private String verificationCode = AppUtils.generateVerificationCode();
    private boolean enabled = true;
    @URL(message = "Invalid picture URL")
    private String picture;
    private String source;
    
    public AppUser(@NotBlank(message = "The username is required") String username) {
        this.username = username;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public void setAdmin(boolean admin) {
        setRole(admin ? "ADMIN" : "USER");
    }
    
    public String getFullName() {
        return StringUtils.join(new String[]{getFirstName(), getLastName()}, " ");
    }
    
    public void setFullName(String fullName) {
        if(StringUtils.isBlank(fullName)) {
            this.firstName = "Unnamed";
            return;
        }
        String[] arr = fullName.split(" ");
        if (arr.length == 1) {
            this.firstName = arr[0];
        } else if (arr.length >= 2) {
            this.firstName = arr[0];
            this.lastName = StringUtils.join(arr, " ", 1, arr.length);
        }
    }
}
