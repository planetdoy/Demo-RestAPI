package com.example.restapi.user;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/users/{id}")
    public User user(@PathVariable Long id) {
        User user = userDaoService.findById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID [%s] not found", id));
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> join(@Validated @RequestBody User user) {
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        User user = userDaoService.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID [%s] not found", id));
        }
    }

    @PutMapping("/users/{id}")
    public User modifiedUser(@PathVariable Long id, @RequestBody User user) {

        User modifiedUser = userDaoService.modifyById(id, user);

        if (modifiedUser == null) {
            throw new UserNotFoundException(String.format("ID [%s] not found", id));
        }

        return modifiedUser;
    }
}
