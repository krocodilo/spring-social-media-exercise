package com.bring.social.jpa;


import com.bring.social.models.UserCredentials;
import com.bring.social.models.requests.NewUserRequest;
import com.bring.social.models.jpa.PostEntity;
import com.bring.social.models.jpa.UserEntity;
import com.bring.social.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")    // -> /users
public class UserResource {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CredentialsRepository credsRepository;

    public UserResource(UserRepository userRepository, PostRepository postRepository,
                        CredentialsRepository credsRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.credsRepository = credsRepository;
    }

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

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(request, user);
        userRepository.save(user);

        // TODO encrypt pass

        credsRepository.save(new UserCredentials(user, request.getPassword(), request.getRole()));

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
        }catch (Exception ignore){
            return false;
        }
        return true;
    }
}
