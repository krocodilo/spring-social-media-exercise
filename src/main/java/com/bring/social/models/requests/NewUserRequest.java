package com.bring.social.models.requests;

import com.bring.social.models.jpa.UserEntity;

public class NewUserRequest extends UserEntity {

//    @Size(min=16, message="Name must have at least 16 char")
    private String password;

    private String role;

    public NewUserRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
