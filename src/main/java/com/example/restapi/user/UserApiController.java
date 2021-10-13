package com.example.restapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserDaoService userDaoService;
    private final UserRepository userRepository;


    @GetMapping("/users")
    public List<User> users() {
        List<User> users = userDaoService.findAll();
        return users;
    }

    @GetMapping("/users/{id}")
    public  EntityModel<User> user(@PathVariable Long id) {
//        User user = userDaoService.findById(id);
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("ID [%s] not found", id));
        }

        //hateoas ...
        EntityModel<User> entityModel = EntityModel.of(user.get());
        entityModel.add(
                linkTo(methodOn(this.getClass())
                                .users())
                        .withRel("all-users"));
        return entityModel;
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


    //@PostMapping("/users")
    public ResponseEntity<User> joinV1(@Validated @RequestBody User user) {
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
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
