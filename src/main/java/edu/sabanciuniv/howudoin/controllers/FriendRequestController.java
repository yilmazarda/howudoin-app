package edu.sabanciuniv.howudoin.controllers;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import edu.sabanciuniv.howudoin.services.FriendRequestService;
import edu.sabanciuniv.howudoin.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;

    // DTO to handle the incoming request
    private static class FriendRequestIdDTO {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @GetMapping("/requests")
    public List<FriendRequest> getAllFriendRequests() {
        String authenticatedEmail = getAuthenticatedUserEmail();
        return friendRequestService.getFriendRequestsByUserEmail(authenticatedEmail);
    }

    @PostMapping("/friends/add")
    public ResponseEntity<FriendRequest> addRequest(@RequestBody FriendRequest friendRequest) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        friendRequest.setSenderEmail(authenticatedEmail);

        if (!authenticatedEmail.equals(friendRequest.getSenderEmail())) {
            return ResponseEntity.status(403).body(null);
        }

        friendRequestService.addFriendRequest(friendRequest);
        return ResponseEntity.ok(friendRequest);
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequestIdDTO requestBody) {
        String authenticatedEmail = getAuthenticatedUserEmail();

        try {
            ObjectId requestId = new ObjectId(requestBody.getId());
            Optional<FriendRequest> friendRequestOpt = friendRequestService.getFriendRequestById(requestId);

            FriendRequest friendRequest = friendRequestOpt.get();

            if (!authenticatedEmail.equals(friendRequest.getReceiverEmail())) {
                return ResponseEntity.status(403).body("You are not authorized to accept this request");
            }

            friendRequestService.acceptFriendRequest(requestId);

            String senderEmail = friendRequest.getSenderEmail();
            String receiverEmail = friendRequest.getReceiverEmail();
            userService.addFriendByEmail(senderEmail, receiverEmail);

            return ResponseEntity.ok("Friend request is accepted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ObjectId format.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to accept friend request: " + e.getMessage());
        }
    }

    @PostMapping("/friends/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestBody FriendRequestIdDTO requestBody) {
        String authenticatedEmail = getAuthenticatedUserEmail();

        try {
            ObjectId requestId = new ObjectId(requestBody.getId());
            Optional<FriendRequest> friendRequestOpt = friendRequestService.getFriendRequestById(requestId);

            if (!friendRequestOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Friend request not found");
            }

            FriendRequest friendRequest = friendRequestOpt.get();

            if (!authenticatedEmail.equals(friendRequest.getReceiverEmail())) {
                return ResponseEntity.status(403).body("You are not authorized to reject this request");
            }

            friendRequestService.rejectFriendRequest(requestId);
            return ResponseEntity.ok("Friend request is rejected.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ObjectId format.");
        } catch (Exception e) {
            System.err.println("Error rejecting friend request: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to reject friend request: " + e.getMessage());
        }
    }

    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}