package com.example.rest.dvdrental.v2.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class PasswordRequest {
    private String password;
    private String confirmPassword;
    
    public boolean match() {
        return StringUtils.equals(password, confirmPassword);
    }
}
