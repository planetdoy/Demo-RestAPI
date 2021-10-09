package com.example.restapi.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserApiController {

    private UserDaoService userDaoService;

    public UserApiController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @GetMapping("/users")
    public List<User> users() {
        List<User> users = userDaoService.findAll();
        return users;
    }

    @GetMapping("/users/{userId}")
    public User user(@PathVariable Long userId){
        User user = userDaoService.findById(userId);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID [%s] not found",userId));
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> join(@RequestBody User user) {
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
