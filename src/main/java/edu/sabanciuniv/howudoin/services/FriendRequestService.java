package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.repositories.FriendRequestRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserService userService; // Add this to check if users are already friends

    public List<FriendRequest> getAllFriendRequests() {
        return friendRequestRepository.findAll();
    }

    public void addFriendRequest(FriendRequest friendRequest) {
        // Check if users are already friends
        if (userService.areFriends(friendRequest.getSenderEmail(), friendRequest.getReceiverEmail())) {
            throw new IllegalStateException("You are already friends with this user");
        }

        // Check if there's an existing pending request
        List<FriendRequest> existingRequests = friendRequestRepository.findBySenderEmailOrReceiverEmail(
                friendRequest.getSenderEmail(),
                friendRequest.getReceiverEmail()
        );

        for (FriendRequest existing : existingRequests) {
            if (existing.getSenderEmail().equals(friendRequest.getSenderEmail())
                    && existing.getReceiverEmail().equals(friendRequest.getReceiverEmail())
                    && !existing.getAccepted()) {
                throw new IllegalStateException("A friend request to this user already exists");
            }
        }

        friendRequest.setAccepted(false);
        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(ObjectId friendRequestId) {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findById(friendRequestId);
        if(friendRequestOpt.isPresent()) {
            FriendRequest friendRequest = friendRequestOpt.get();

            // Check if they're already friends (in case of concurrent requests)
            if (userService.areFriends(friendRequest.getSenderEmail(), friendRequest.getReceiverEmail())) {
                friendRequestRepository.deleteById(friendRequestId);
                throw new IllegalStateException("You are already friends with this user");
            }

            friendRequest.setAccepted(true);
            friendRequestRepository.delete(friendRequest);
        }
        else {
            throw new RuntimeException("Friend request with ID " + friendRequestId + " not found");
        }
    }

    public void rejectFriendRequest(ObjectId friendRequestId) {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findById(friendRequestId);
        if(friendRequestOpt.isPresent()) {
            friendRequestRepository.deleteById(friendRequestId);
        }
        else {
            throw new RuntimeException("Friend request with ID " + friendRequestId + " not found");
        }
    }

    public Optional<FriendRequest> getFriendRequestById(ObjectId friendRequestId) {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findById(friendRequestId);
        if (friendRequestOpt.isPresent()) {
            return friendRequestOpt;
        } else {
            throw new RuntimeException("Friend request not found for ID: " + friendRequestId);
        }
    }

    public List<FriendRequest> getFriendRequestsByUserEmail(String email) {
        return friendRequestRepository.findByReceiverEmail(email);
    }
}