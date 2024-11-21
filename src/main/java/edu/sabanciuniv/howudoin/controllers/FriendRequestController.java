package edu.sabanciuniv.howudoin.controllers;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.services.FriendRequestService;
import edu.sabanciuniv.howudoin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    // Endpoint to get all friend requests for the authenticated user
    @GetMapping("/requests")
    public List<FriendRequest> getAllFriendRequests() {
        String authenticatedEmail = getAuthenticatedUserEmail();
        return friendRequestService.getFriendRequestsByUserEmail(authenticatedEmail);
    }

    // Endpoint to add a friend request from the authenticated user
    @PostMapping("/friends/add")
    public ResponseEntity<FriendRequest> addRequest(@RequestBody FriendRequest friendRequest) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        friendRequest.setSenderEmail(authenticatedEmail);

        // Only allow the authenticated user to send a request
        if (!authenticatedEmail.equals(friendRequest.getSenderEmail())) {
            return ResponseEntity.status(403).body(null); // Forbidden if the sender does not match
        }

        friendRequestService.addFriendRequest(friendRequest);
        return ResponseEntity.ok(friendRequest);
    }

    // Endpoint to accept a friend request
    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody String requestId) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        System.out.println("Authenticated Email: " + authenticatedEmail);

        // Get the friend request by its ID
        FriendRequest friendRequest = friendRequestService.getFriendRequestById(requestId)
                .orElse(null);


        if (friendRequest == null) {
            System.err.println("No friend request found with ID: " + requestId);
            return ResponseEntity.status(404).body("Friend request not found");
        }

        if (!authenticatedEmail.equals(friendRequest.getReceiverEmail())) {
            System.err.println("Unauthorized access. Authenticated email: " + authenticatedEmail);
            return ResponseEntity.status(403).body("You are not authorized to accept this request");
        }

        // Accept the friend request
        friendRequestService.acceptFriendRequest(requestId);

        // Add the users as friends in the system
        String senderEmail = friendRequest.getSenderEmail();
        String receiverEmail = friendRequest.getReceiverEmail();
        userService.addFriendByEmail(senderEmail, receiverEmail);

        return ResponseEntity.ok("Friend request is accepted.");
    }

    // Helper method to get the authenticated user's email from the security context
    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
