package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.sabanciuniv.howudoin.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean areFriends(String userEmail1, String userEmail2) {
        // Get both users from repository
        Optional<User> user1Opt = userRepository.findByEmail(userEmail1);
        Optional<User> user2Opt = userRepository.findByEmail(userEmail2);

        // If either user doesn't exist, they can't be friends
        if (!user1Opt.isPresent() || !user2Opt.isPresent()) {
            return false;
        }

        User user1 = user1Opt.get();
        User user2 = user2Opt.get();

        // Check if user2's email is in user1's friends list
        List<String> user1Friends = user1.getFriends();
        if (user1Friends != null) {
            return user1Friends.contains(user2.getEmail());
        }

        return false;
    }



    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail (String email) {
        User user = userRepository.findById(email).orElse(null);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }
    public void addFriendByEmail(String userEmail1, String userEmail2) {
        // Get both users
        Optional<User> user1Opt = userRepository.findByEmail(userEmail1);
        Optional<User> user2Opt = userRepository.findByEmail(userEmail2);

        if (!user1Opt.isPresent() || !user2Opt.isPresent()) {
            throw new RuntimeException("One or both users not found");
        }

        User user1 = user1Opt.get();
        User user2 = user2Opt.get();

        // Initialize friends lists if they don't exist
        if (user1.getFriends() == null) {
            user1.setFriends(new ArrayList<>());
        }
        if (user2.getFriends() == null) {
            user2.setFriends(new ArrayList<>());
        }

        // Add each user to the other's friends list if they're not already there
        if (!user1.getFriends().contains(user2.getEmail())) {
            user1.getFriends().add(user2.getEmail());
        }
        if (!user2.getFriends().contains(user1.getEmail())) {
            user2.getFriends().add(user1.getEmail());
        }

        // Save both users
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
