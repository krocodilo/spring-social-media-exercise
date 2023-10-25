package com.bring.social.rest;


import com.bring.social.exceptions.UserNotFoundException;
import com.bring.social.jpa.CredentialsRepository;
import com.bring.social.jpa.PostRepository;
import com.bring.social.jpa.UserRepository;
import com.bring.social.models.AuthorityType;
import com.bring.social.models.jpa.PostEntity;
import com.bring.social.models.jpa.UserCredentials;
import com.bring.social.models.jpa.UserEntity;
import com.bring.social.models.requests.NewUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("users")    // -> /users
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CredentialsRepository credsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public List<UserEntity> getALlUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserEntity getOneUser(@PathVariable int id) {
        Optional<UserEntity> usr = userRepository.findById(id);
        if(usr.isEmpty())
            throw new UserNotFoundException("User Not Found: " + id);
        return usr.get();
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody NewUserRequest request){

        // Save user to DB
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(request, user);
        userRepository.save(user);

        // Calculate the password hash
        String passwordHash = passwordEncoder.encode( request.getPassword() );

        // Create a set of the requested authorities. In prod, users should not be able to become admins doing this
        Set<AuthorityType> authorities = new HashSet<>(
                request.getAuthorities().stream()
                        .map(authName -> AuthorityType.valueOf( authName.toUpperCase() ))
                        .toList()
        );

        // Save user credentials to DB
        credsRepository.save(new UserCredentials(user, passwordHash, authorities));

        // Return the location to the new user profile
        URI newUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(user.getId()).toUri();

        // Return Status Code 201 (Created) with the created location
        return ResponseEntity.created(newUri).build();
    }

    @GetMapping("/{id}/posts")
    public List<PostEntity> retrievePostsForUser(@PathVariable int id) {
        Optional<UserEntity> usr = userRepository.findById(id);
        if(usr.isEmpty())
            throw new UserNotFoundException("User Not Found: " + id);

        return  usr.get().getPosts();
    }

    @PostMapping("/{id}/posts")
    public ResponseEntity<PostEntity> createPostForUser(@PathVariable int id, @Valid @RequestBody PostEntity post) {
        Optional<UserEntity> usr = userRepository.findById(id);
        if(usr.isEmpty())
            throw new UserNotFoundException("User Not Found: " + id);

        post.setUser(usr.get());
        PostEntity savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(savedPost.getId()).toUri();

        // Return Status Code 201 (Created) with the created location
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/deleteAll")
    public boolean deleteAll(){
        try{
            userRepository.deleteAll();
            postRepository.deleteAll();
        }catch (Exception ignore){
            return false;
        }
        return true;
    }
}
