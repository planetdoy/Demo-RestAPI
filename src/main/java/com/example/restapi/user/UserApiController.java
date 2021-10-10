package com.example.restapi.user;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        //hateoas
        EntityModel<User> entityModel = new EntityModel<>(user);

        return user;
    }


    /**
     * HATEOAS 적용
     */
    @GetMapping("/users2")
    public ResponseEntity<CollectionModel<EntityModel<User>>> retrieveUserList2() {
        List<EntityModel<User>> result = new ArrayList<>();
        List<User> users = userDaoService.findAll();

        for (User user : users) {
            EntityModel entityModel = EntityModel.of(user);
            entityModel.add(linkTo(methodOn(this.getClass()).users()).withSelfRel());

            result.add(entityModel);
        }

        return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).users()).withSelfRel()));
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
