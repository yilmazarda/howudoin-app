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
    public ResponseEntity<?> addRequest(@RequestBody FriendRequest friendRequest) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        friendRequest.setSenderEmail(authenticatedEmail);

        if (!authenticatedEmail.equals(friendRequest.getSenderEmail())) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Unauthorized request"));
        }

        try {
            friendRequestService.addFriendRequest(friendRequest);
            return ResponseEntity.ok()
                    .body(Map.of("message", "Friend request sent successfully", "data", friendRequest));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to send friend request: " + e.getMessage()));
        }
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequestIdDTO requestBody) {
        String authenticatedEmail = getAuthenticatedUserEmail();

        try {
            ObjectId requestId = new ObjectId(requestBody.getId());
            Optional<FriendRequest> friendRequestOpt = friendRequestService.getFriendRequestById(requestId);

            FriendRequest friendRequest = friendRequestOpt.get();

            if (!authenticatedEmail.equals(friendRequest.getReceiverEmail())) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "You are not authorized to accept this request"));
            }

            try {
                friendRequestService.acceptFriendRequest(requestId);
                String senderEmail = friendRequest.getSenderEmail();
                String receiverEmail = friendRequest.getReceiverEmail();
                userService.addFriendByEmail(senderEmail, receiverEmail);
                return ResponseEntity.ok()
                        .body(Map.of("message", "Friend request accepted successfully"));
            } catch (IllegalStateException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", e.getMessage()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid ObjectId format"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to accept friend request: " + e.getMessage()));
        }
    }

    @PostMapping("/friends/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestBody FriendRequestIdDTO requestBody) {
        String authenticatedEmail = getAuthenticatedUserEmail();

        try {
            ObjectId requestId = new ObjectId(requestBody.getId());
            Optional<FriendRequest> friendRequestOpt = friendRequestService.getFriendRequestById(requestId);

            if (!friendRequestOpt.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Friend request not found"));
            }

            FriendRequest friendRequest = friendRequestOpt.get();

            if (!authenticatedEmail.equals(friendRequest.getReceiverEmail())) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "You are not authorized to reject this request"));
            }

            friendRequestService.rejectFriendRequest(requestId);
            return ResponseEntity.ok()
                    .body(Map.of("message", "Friend request rejected successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid ObjectId format"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to reject friend request: " + e.getMessage()));
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