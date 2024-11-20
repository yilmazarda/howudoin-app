package edu.sabanciuniv.howudoin.controllers;


import edu.sabanciuniv.howudoin.models.User;
import edu.sabanciuniv.howudoin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user)
    {
        userService.addUser(user);
        return user;
    }

    @GetMapping("/friends")
    public List<String> getAllFriends(@RequestParam String email)
    {
        User user = userService.getUserByEmail(email);
        return user.getFriends();
    }
}
