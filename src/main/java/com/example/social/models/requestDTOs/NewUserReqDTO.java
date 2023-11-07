package com.example.social.models.requestDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record NewUserReqDTO (
        @NotNull
        @Size(min=2, message="Name must have at least 2 char")
        String username,

        @NotNull
        @Past(message = "Birth date must be in past")
        @JsonFormat(pattern="yyyy-MM-dd")
        LocalDate birth_date,

        @NotNull
//        @Size(min=16, message="Name must have at least 16 char")
        String password,

        @NotNull
//        @NotEmpty
        Set<String> authorities
){}
