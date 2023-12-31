package com.example.social.security;

import com.example.social.jpa.CredentialsRepository;
import com.example.social.jpa.UserRepository;
import com.example.social.models.jpa.UserCredentials;
import com.example.social.models.jpa.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Spring Security automatically uses this method to get the details of a user. Those details
    // will then be used to compare the credentials received in a request.

    private final UserRepository userRepository;
    private final CredentialsRepository credsRepository;

    public CustomUserDetailsService(UserRepository userRepository, CredentialsRepository credsRepository) {
        this.userRepository = userRepository;
        this.credsRepository = credsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Username '"+username+"' does nto exist.");

        Optional<UserCredentials> creds = credsRepository.findById( user.getId() );
        if(creds.isEmpty())
            throw new NoSuchElementException("There's no credentials for user '"+username+"'.");

        return new User(
                user.getUsername(),
                creds.get().getEncodedPassword(),
//                List.of(new SimpleGrantedAuthority( "ROLE_ADMIN" ))
                creds.get().getAuthorities().stream().map(
                        SimpleGrantedAuthority::new
                ).toList()
        );
    }
}
