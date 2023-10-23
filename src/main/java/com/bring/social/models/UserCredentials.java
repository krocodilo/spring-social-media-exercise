package com.bring.social.models;

import com.bring.social.models.jpa.UserEntity;
import jakarta.persistence.*;

@Entity(name="passwd")  // DB Table name
public class UserCredentials {

    @Id
    private Integer id;

    @MapsId
    @JoinColumn(name="user_id")         // name of the shared/joined column
    @OneToOne(fetch = FetchType.LAZY, optional = false)   //because we don't want to fetch the user object
    private UserEntity user;

    private String encodedPassword;

    private String authority;   // for spring security

    public UserCredentials() {
    }

    public UserCredentials(UserEntity user, String encodedPassword, String authority) {
        this.user = user;
        this.encodedPassword = encodedPassword;
        this.authority = authority;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
