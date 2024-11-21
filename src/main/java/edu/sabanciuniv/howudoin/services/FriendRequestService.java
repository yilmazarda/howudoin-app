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

    public List<FriendRequest> getAllFriendRequests()
    {
        return friendRequestRepository.findAll();
    }

    public void addFriendRequest(FriendRequest friendRequest) {
        friendRequest.setAccepted(false);
        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(ObjectId friendRequestId) {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findById(friendRequestId);
        if(friendRequestOpt.isPresent()) {
            FriendRequest friendRequest = friendRequestOpt.get();
            friendRequest.setAccepted(true);
            friendRequestRepository.save(friendRequest);
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
        // Fetch all friend requests where the user is either the sender or the receiver
        return friendRequestRepository.findBySenderEmailOrReceiverEmail(email, email);
    }
}
