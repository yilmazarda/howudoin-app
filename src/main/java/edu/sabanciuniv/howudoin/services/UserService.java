package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.sabanciuniv.howudoin.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
    public void addFriendByEmail (String senderEmail, String receiverEmail) {
        User senderUser = userRepository.findById(senderEmail).orElse(null);

        User receiverUser = userRepository.findById(receiverEmail).orElse(null);

        if(senderUser != null && receiverUser != null) {
            senderUser.getFriends().add(receiverUser.getEmail());
            receiverUser.getFriends().add(senderUser.getEmail());

            userRepository.save(senderUser);
            userRepository.save(receiverUser);
        }
        else
        {
            throw new RuntimeException("User not found");
        }
    }
}
