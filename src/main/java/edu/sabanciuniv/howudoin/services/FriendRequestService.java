package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.repositories.FriendRequestRepository;
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
        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(String friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendRequest.setAccepted(true);
        friendRequestRepository.delete(friendRequest); // Eğer kabul edilen talepleri silmeniz gerekiyorsa bu satır doğru.
    }

    public Optional<FriendRequest> getFriendRequestById(String friendRequestId) {
        return friendRequestRepository.findById(friendRequestId);
    }


    public List<FriendRequest> getFriendRequestsByUserEmail(String email) {
        // Fetch all friend requests where the user is either the sender or the receiver
        return friendRequestRepository.findBySenderEmailOrReceiverEmail(email, email);
    }
}
