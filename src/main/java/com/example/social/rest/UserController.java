package com.example.social.rest;


import com.example.social.exceptions.UserNotFoundException;
import com.example.social.jpa.CredentialsRepository;
import com.example.social.jpa.PostRepository;
import com.example.social.jpa.UserRepository;
import com.example.social.models.AuthType;
import com.example.social.models.jpa.PostEntity;
import com.example.social.models.jpa.UserCredentials;
import com.example.social.models.jpa.UserEntity;
import com.example.social.models.mappers.UserMapper;
import com.example.social.models.requestDTOs.NewUserReqDTO;
import com.example.social.models.responseDTOs.UserRespDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CredentialsRepository credsRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserController(UserRepository userRepository, PostRepository postRepository,
                          CredentialsRepository credsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.credsRepository = credsRepository;
        this.passwordEncoder = passwordEncoder;

        userMapper = UserMapper.MAPPER;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(){
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<UserRespDTO> getALlUsers() {
        return userRepository.findAll().stream()
                .map( userMapper::toUserRespDTO).toList();
    }

    @GetMapping("/{id}")
    public UserRespDTO getOneUser(@PathVariable int id) {
        Optional<UserEntity> usr = userRepository.findById(id);
        if(usr.isEmpty())
            throw new UserNotFoundException("User Not Found: " + id);

        return userMapper.toUserRespDTO( usr.get() );
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody NewUserReqDTO request){

        // TODO: if request fails Spring Validation, 401 code is returned without explanation to the user. Must be improved

        if(userRepository.findByUsername(request.username()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");

        // Save user to DB
        UserEntity user = userMapper.toUserEntity(request);
        userRepository.save( user );

        // Calculate the password hash
        String passwordHash = passwordEncoder.encode( request.password() );

        // Create a set of the requested authorities. In prod, users should not be able to become admins doing this
        Set<String> authorities = new HashSet<>(
                request.authorities().stream()
                        .filter( authName -> AuthType.getByName(authName).isPresent() )
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

    @PatchMapping("/{username}/addAuthority")
    public ResponseEntity<String> addAuthorityToUser(@PathVariable String username,@RequestBody String authority){

        // Check if authority exists
        Optional<AuthType> auth = AuthType.getByName(authority);
        if( auth.isEmpty() )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That authority does not exist.");

        // Find user
        UserEntity user = userRepository.findByUsername(username);
        if(user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That user does not exist.");

        // Get user authorities list
        Optional<UserCredentials> tmp = credsRepository.findById( user.getId() );
        if(tmp.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to find user credentials.");
        UserCredentials creds = tmp.get();

        // Save changes
        creds.getAuthorities().add( auth.get().name() );
        credsRepository.save( creds );

        return ResponseEntity.status(HttpStatus.OK).build();
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
