package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.models.User;
import edu.sabanciuniv.howudoin.repositories.FriendRequestRepository;
import edu.sabanciuniv.howudoin.repositories.UserRepository;
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

    public void acceptFriendRequest(int friendRequestId) {
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

    public FriendRequest getFriendRequestById(int friendRequestId) {
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
}
