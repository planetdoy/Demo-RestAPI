package com.example.restapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Optional<User> findById(Long id) {
        Optional<User> user = Optional.ofNullable(em.find(User.class, id));
        return user;
    }
}
