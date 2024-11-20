package edu.sabanciuniv.howudoin.controllers;


import edu.sabanciuniv.howudoin.models.Group;
import edu.sabanciuniv.howudoin.models.GroupMessage;
import edu.sabanciuniv.howudoin.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        if(groups.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(groups);
    }


    @PostMapping("/groups/create")
    public Group addGroup(@RequestBody Group group)
    {
        groupService.addGroup(group);

        return group;
    }

    @PostMapping("/groups/{groupId}/add-member")
    public Group addMemberToGroup(@PathVariable String groupId, @RequestParam String userEmail)
    {
        // Fetch the group
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        // Add the userEmail to the group
        groupService.addMemberToGroup(groupId, userEmail);

        // Return the updated group
        return groupService.getGroupById(groupId);
    }


    @GetMapping("/groups/{groupId}/messages")
    public List<GroupMessage> getGroupMessages(@PathVariable String groupId)
    {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        return groupService.getGroupMessages(groupId);
    }

    @PostMapping("/groups/{groupId}/send")
    public GroupMessage sendMessages(@PathVariable String groupId, @RequestBody GroupMessage groupMessage)
    {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        return groupService.sendGroupMessage(groupId, groupMessage);
    }



    @GetMapping("/groups/{groupId}/members")
    public List<String> getMembers(@PathVariable String groupId)
    {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        return groupService.getMembers(groupId);
    }


}
