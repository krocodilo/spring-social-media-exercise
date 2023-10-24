package com.bring.social.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // during startup, Spring will excecute this class
public class SpringSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                req -> req
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()  //anyone can create new user
                        .anyRequest().authenticated()
//                        .anyRequest().denyAll()   // just as example
        )
        .httpBasic(Customizer.withDefaults())  // Http Basic Standards

        .csrf(AbstractHttpConfigurer::disable); // temp
        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        // Will become the default Password Encoder
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource datasource) {
//        // DataSource contains the DB-related properties defined in application.properties
//        return new JdbcUserDetailsManager(datasource);
//    }


//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("user").password("dummypassword")
//                .authorities("admin")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin);
//    }

}
