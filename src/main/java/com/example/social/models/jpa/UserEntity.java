package com.example.social.models.jpa;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

//Declare as Entity because we want JPA to manage this
@Entity(name = "users")   //DB Table name: h2users
public class UserEntity {

    public UserEntity(){}  //to be used by JPA

//    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;

//    @Size(min=2, message="Name must have at least 2 char")
    @Column(unique = true)
    private String username;

//    @JsonProperty("birth_date")
//    @Past(message = "Birth date must be in past")
    private LocalDate birthDate;

    //    @JsonIgnore // Because posts must not be part of the JSON request/response for the User bean
    // Opposite of what is in Post entity:
    @OneToMany(mappedBy = "user")   //The field in Post that owns this relationship
    private List<PostEntity> posts;

//    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false,
        cascade = CascadeType.REMOVE)   //Only update creds table when user table is updated to remove (by removing it as well)
    private UserCredentials credentials;

    public UserEntity(Integer id, String username, LocalDate birthDate) {
        this.id = id;
        this.username = username;
        this.birthDate = birthDate;
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

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

}
