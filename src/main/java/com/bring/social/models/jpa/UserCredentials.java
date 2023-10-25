package com.bring.social.models.jpa;

import com.bring.social.models.AuthorityType;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name="passwd")  // DB Table name
public class UserCredentials {

    @Id
    private Integer id;

    @MapsId
    @JoinColumn(name="user_id")         // name of the shared/joined column
    @OneToOne(fetch = FetchType.LAZY, optional = false)   //because we don't want to fetch the user object
    private UserEntity user;

    private String encodedPassword;

    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    private Set<AuthorityType> authorities;   // for spring security

    public UserCredentials() {
    }

    public UserCredentials(UserEntity user, String encodedPassword, Set<AuthorityType> authority) {
        this.user = user;
        this.encodedPassword = encodedPassword;
        this.authorities = authority;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public Set<AuthorityType> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<AuthorityType> authorities) {
        this.authorities = authorities;
    }
}
