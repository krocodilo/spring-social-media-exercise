package com.bring.social.security;

import com.bring.social.jpa.CredentialsRepository;
import com.bring.social.jpa.UserRepository;
import com.bring.social.models.jpa.UserCredentials;
import com.bring.social.models.jpa.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserRepository userRepository;
    @Autowired
    CredentialsRepository credsRepository;

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
//                List.of(new SimpleGrantedAuthority( creds.get().getAuthority() ))
                creds.get().getAuthorities().stream().map(
                        authType -> new SimpleGrantedAuthority( authType.name() )
                ).toList()
        );
    }
}
