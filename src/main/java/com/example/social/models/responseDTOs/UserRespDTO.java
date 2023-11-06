package com.example.social.models.responseDTOs;

import java.time.LocalDate;

public class UserRespDTO {

    private Integer id;
    private String username;
    private LocalDate birthDate;

    public UserRespDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
