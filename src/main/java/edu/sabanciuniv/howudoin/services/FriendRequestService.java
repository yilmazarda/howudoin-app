package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.repositories.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public List<FriendRequest> getAllFriendRequests()
    {
        return friendRequestRepository.findAll();
    }

    public void addFriendRequest(FriendRequest friendRequest) {
        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(String friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId).get();
        if(friendRequest == null)
        {
            throw new RuntimeException("Friend request not found");
        }
        else
        {
            friendRequest.setAccepted(true);
            friendRequestRepository.delete(friendRequest);
        }
    }

    public FriendRequest getFriendRequestById(String friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId).get();
        if(friendRequest == null)
        {
            throw new RuntimeException("Friend request not found");
        }
        else
        {
            return friendRequest;
        }
    }

    public List<FriendRequest> getFriendRequestsByUserEmail(String email) {
        // Fetch all friend requests where the user is either the sender or the receiver
        return friendRequestRepository.findBySenderEmailOrReceiverEmail(email, email);
    }
}
