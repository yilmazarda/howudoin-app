package edu.sabanciuniv.howudoin.controllers;

import edu.sabanciuniv.howudoin.models.Group;
import edu.sabanciuniv.howudoin.models.GroupMessage;
import edu.sabanciuniv.howudoin.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<Group> addGroup(@RequestBody Group group) {
        // Set the creator's email from the authenticated user
        String creatorEmail = getAuthenticatedUserEmail();

        // Add the authenticated user as the first member of the group
        group.getUsers().add(creatorEmail);

        // Create the group
        groupService.addGroup(group);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<?> addMemberToGroup(@PathVariable String groupId, @RequestParam String memberEmail) {
        // Check if the authenticated user is part of the group
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User not authorized to add members.");
        }

        // Add the member to the group
        groupService.addMemberToGroup(groupId, memberEmail);

        // Return the updated group
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<?> getGroupMessages(@PathVariable String groupId) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User not authorized to view group messages.");
        }

        return ResponseEntity.ok(groupService.getGroupMessages(groupId));
    }

    @PostMapping("/{groupId}/send")
    public ResponseEntity<?> sendMessages(@PathVariable String groupId, @RequestBody GroupMessage groupMessage) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        groupMessage.setGroupId(groupId);
        groupMessage.setSenderEmail(authenticatedEmail);
        groupMessage.setSentAt(LocalDateTime.now());
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User not authorized to send messages.");
        }

        GroupMessage savedMessage = groupService.sendGroupMessage(groupId, groupMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getMembers(@PathVariable String groupId) {
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User not authorized to view group members.");
        }

        List<String> members = groupService.getMembers(groupId);
        return ResponseEntity.ok(members);
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
