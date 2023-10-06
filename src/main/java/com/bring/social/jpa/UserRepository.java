package com.bring.social.jpa;

import com.bring.social.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    //It will manage the User entity, which has an ID field of type Integer

    // these methods are automatically implemented by Spring JPA:
    List<User> findByName(String name);
    List<User> findByBirthDate(LocalDate birthDate);
}
