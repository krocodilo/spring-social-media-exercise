package com.bring.social.jpa;

import com.bring.social.models.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<UserCredentials, Integer> {

}