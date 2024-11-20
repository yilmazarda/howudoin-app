package edu.sabanciuniv.howudoin.controllers;


import edu.sabanciuniv.howudoin.models.User;
import edu.sabanciuniv.howudoin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public List<String> getAllFriends() {
        // Extract authenticated user's email from the Security Context
        String authenticatedEmail = getAuthenticatedUserEmail();
        User user = userService.getUserByEmail(authenticatedEmail);
        return user.getFriends();
    }

    // Helper method to get the authenticated user's email
    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
