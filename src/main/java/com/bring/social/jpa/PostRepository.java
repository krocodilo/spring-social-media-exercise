package com.bring.social.jpa;

import com.bring.social.models.jpa.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    //It will manage the User entity, which has an ID field of type Integer
}
