package com.bring.social.models.jpa;

import com.bring.social.models.jpa.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity     // Will create a DB table named "Post"
public class PostEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min = 10, message = "erroer ")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)  // because when we fetch the post, we don't want to
                            // fetch the details of the user (which would be the default)
    @JsonIgnore
    private UserEntity user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
