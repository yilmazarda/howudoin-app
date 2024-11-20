package edu.sabanciuniv.howudoin.controllers;

import edu.sabanciuniv.howudoin.models.Group;
import edu.sabanciuniv.howudoin.models.GroupMessage;
import edu.sabanciuniv.howudoin.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public Group addGroup(@RequestBody Group group) {
        // Set the creator's email from the authenticated user
        String creatorEmail = getAuthenticatedUserEmail();

        // Add the authenticated user as the first member of the group
        group.getUsers().add(creatorEmail);

        // Create the group
        groupService.addGroup(group);
        return group;
    }

    @PostMapping("/{groupId}/add-member")
    public Group addMemberToGroup(@PathVariable Integer groupId, @RequestParam String memberEmail) {
        // Check if the authenticated user is part of the group
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            throw new IllegalArgumentException("User not authorized to add members.");
        }

        // Add the member to the group
        groupService.addMemberToGroup(groupId, memberEmail);

        // Return the updated group
        return groupService.getGroupById(groupId);
    }

    @GetMapping("/{groupId}/messages")
    public List<GroupMessage> getGroupMessages(@PathVariable Integer groupId) {
        // Check if the authenticated user is part of the group
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            throw new IllegalArgumentException("User not authorized to view group messages.");
        }

        return groupService.getGroupMessages(groupId);
    }

    @PostMapping("/{groupId}/send")
    public GroupMessage sendMessages(@PathVariable Integer groupId, @RequestBody GroupMessage groupMessage) {
        // Check if the authenticated user is part of the group
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            throw new IllegalArgumentException("User not authorized to send messages.");
        }

        // Set the sender's email from the authenticated user
        groupMessage.setSenderEmail(authenticatedEmail);

        return groupService.sendGroupMessage(groupId, groupMessage);
    }

    @GetMapping("/{groupId}/members")
    public List<String> getMembers(@PathVariable Integer groupId) {
        // Check if the authenticated user is part of the group
        String authenticatedEmail = getAuthenticatedUserEmail();
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        if (!group.getUsers().contains(authenticatedEmail)) {
            throw new IllegalArgumentException("User not authorized to view group members.");
        }

        return groupService.getMembers(groupId);
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
