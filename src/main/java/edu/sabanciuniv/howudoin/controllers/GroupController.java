package edu.sabanciuniv.howudoin.controllers;


import edu.sabanciuniv.howudoin.models.Group;
import edu.sabanciuniv.howudoin.models.GroupMessage;
import edu.sabanciuniv.howudoin.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;


    @PostMapping("/groups/create")
    public Group addGroup(@RequestBody Group group)
    {
        groupService.addGroup(group);
        return group;
    }

    @PostMapping("/groups/{groupId}/add-member")
    public Group addMemberToGroup(@PathVariable Integer groupId, @RequestBody String userEmail)
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
    public List<GroupMessage> getGroupMessages(@PathVariable Integer groupId)
    {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        return groupService.getGroupMessages(groupId);
    }

    @PostMapping("/groups/{groupId}/send")
    public GroupMessage sendMessages(@PathVariable Integer groupId, @RequestBody GroupMessage groupMessage)
    {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        return groupService.sendGroupMessage(groupId, groupMessage);
    }



    @GetMapping("/groups/{groupId}/members")
    public List<String> getMembers(@PathVariable Integer groupId)
    {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        return groupService.getMembers(groupId);
    }


}
