package com.example.social.jpa;

import com.example.social.models.jpa.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<UserCredentials, Integer> {

}
