package com.bring.social.jpa;

import com.bring.social.user.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    //It will manage the User entity, which has an ID field of type Integer
}
