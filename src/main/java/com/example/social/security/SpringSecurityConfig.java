package com.example.social.security;

import com.example.social.models.AuthType;
import com.example.social.security.filters.JwtTokenGeneratorFilter;
import com.example.social.security.filters.JwtTokenValidatorFilter;
import com.example.social.security.filters.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration  // during startup, Spring will excecute this class
public class SpringSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(Customizer.withDefaults());  // Http Basic Standards

        // Validate JWT token, if existent
        http.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class);
        // JWT generation should only happen after request user authentication
        http.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class);

        http.authorizeHttpRequests(
                req -> req
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()  //anyone can create new user
                        .requestMatchers("/users","/users/{id}/*").hasRole( AuthType.ROLE_ADMIN.removeRolePrefix() )
                        .anyRequest().authenticated()

//                        .requestMatchers(HttpMethod.GET,"/users").hasAuthority( AuthorityType.VIEW_USERS.name() )
//                        .anyRequest().denyAll()   // just as example
        );

        // Tell Spring Security to not create JSession IDs, nor HTTP Sessions
        http.sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );

        http.csrf(AbstractHttpConfigurer::disable); // temp
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins( List.of("https://localhost:8080") );
        cors.setAllowedMethods( List.of("*") );
        cors.setAllowCredentials(true);

        cors.setAllowedHeaders( List.of("*") );
        cors.setExposedHeaders( List.of(SecurityConstants.JWT_HEADER) );
        cors.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        // Will become the default Password Encoder
        return new BCryptPasswordEncoder(); //Hashes the passwords. Does not encode, nor encrypt
    }
}
