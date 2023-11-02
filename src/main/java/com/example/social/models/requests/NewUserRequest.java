package com.example.social.models.requests;

import com.example.social.models.jpa.UserEntity;

import java.util.Set;

public class NewUserRequest extends UserEntity {

//    @Size(min=16, message="Name must have at least 16 char")
    private String password;

    private Set<String> authorities;   // user roles

    public NewUserRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
