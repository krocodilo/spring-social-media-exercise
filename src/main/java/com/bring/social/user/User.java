package com.bring.social.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

//Declare as Entity because we want JPA to manage this
@Entity(name = "h2users")   //DB Table name: h2users
public class User {

    protected User(){}  //to be used by JPA

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=2, message="Name must have at least 2 char")
    @JsonProperty("user_name")
    private String name;

    @Past(message = "Birth date must be in past")
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    // Opposite of what is in Post entity:
    @OneToMany(mappedBy = "user")   //The field in Post that owns this relationship
    @JsonIgnore // Because posts must not be part of the JSON response for the User bean
    private List<Post> posts;

    public User(Integer id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
