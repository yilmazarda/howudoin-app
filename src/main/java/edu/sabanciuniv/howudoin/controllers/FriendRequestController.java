package edu.sabanciuniv.howudoin.controllers;


import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.models.User;
import edu.sabanciuniv.howudoin.services.FriendRequestService;
import edu.sabanciuniv.howudoin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;

    /*
    @GetMapping("/user")
    public List<FriendRequest> getAllUsers()
    {
        return friendRequestService.getAllFriendRequests();
    }
    */

    @PostMapping("/friends/add")
    public FriendRequest addRequest(@RequestBody FriendRequest friendRequest)
    {
        friendRequestService.addFriendRequest(friendRequest);
        return friendRequest;
    }

    @PostMapping("/friends/accept")
    public void acceptFriendRequest(@RequestBody int requestId)
    {
        FriendRequest friendRequest = friendRequestService.getFriendRequestById(requestId);
        friendRequestService.acceptFriendRequest(requestId);

        String senderEmail = friendRequest.getSenderEmail();
        String receiverEmail = friendRequest.getReceiverEmail();
        userService.addFriendByEmail(senderEmail, receiverEmail);
    }
}
