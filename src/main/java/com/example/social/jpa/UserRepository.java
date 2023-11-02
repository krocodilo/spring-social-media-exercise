package com.example.social.jpa;

import com.example.social.models.jpa.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    //It will manage the User entity, which has an ID field of type Integer

    // these methods are automatically implemented by Spring JPA:
    UserEntity findByUsername(String username);
    // We can chose to receive a List or a single object. JPA automatically supports both
    List<UserEntity> findByBirthDate(LocalDate birthDate);






//    UserEntity findOneByUsername(String username);
    // Is an alias to "UserEntity findByUsername(String username);"
    // It can also be set to return a list, but not advised, as it wouldnt make sense
}
