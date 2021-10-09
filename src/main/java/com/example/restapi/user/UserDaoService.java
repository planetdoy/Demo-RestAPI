package com.example.restapi.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    public static List<User> users = new ArrayList<>();
    public static Long userCount = 3l;

    static{
        users.add(new User(1l, "A", new Date()));
        users.add(new User(2l, "A", new Date()));
        users.add(new User(3l, "A", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(Long userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userCount);
        }
        users.add(user);
        return user;
    }

    public User deleteById(Long userId) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == userId) {
                iterator.remove();
                return user;
            }
        }
        return null;
    }

    public User modifyById(Long id ,User modifiedUser) {

        User user = findById(id);
        if (user == null) {
            return null;
        }else{
            user.setDate(modifiedUser.getDate());
            user.setName(modifiedUser.getName());

            return user;
        }
    }

}
