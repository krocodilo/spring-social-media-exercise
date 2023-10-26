package com.bring.social.models;

import java.util.Arrays;
import java.util.Optional;

public enum AuthType {
    // Individual Authorities/Privileges
    VIEW_USERS,
    DELETE_EVERYTHING,

    // Roles
    ROLE_ADMIN,
    ROLE_MANAGER,
    ROLE_USER;

    public String removeRolePrefix() {
        return this.name().substring( "ROLE_".length() );
    }

    public static Optional<AuthType> getByName(String authority) {
        return Arrays.stream( AuthType.values() )
                .filter( value -> value.name().equals(authority) )
                .findFirst();
    }
}
