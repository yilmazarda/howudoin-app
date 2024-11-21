package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.Group;
import edu.sabanciuniv.howudoin.models.GroupMessage;
import edu.sabanciuniv.howudoin.repositories.GroupMessageRepository;
import edu.sabanciuniv.howudoin.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    public List<Group> getAllGroups()
    {
        return groupRepository.findAll();
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    public Group getGroupById (String groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);

        if(group == null) {
            throw new RuntimeException("User not found");
        }

        return group;
    }

    public void addMemberToGroup(String groupId, String userEmail) {
        Group group = groupRepository.findById(groupId).orElse(null);

        if(group == null) {
            throw new RuntimeException("User not found");
        }
        group.getUsers().add(userEmail);

        groupRepository.save(group);
    }

    public List<String> getMembers(String groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null) {
            throw new RuntimeException("User not found");
        }

        return group.getUsers();
    }

    public List<GroupMessage> getGroupMessages(String groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null) {
            throw new RuntimeException("Group not found");
        }

        return group.getGroupMessages();
    }

     public GroupMessage sendGroupMessage(String groupId, GroupMessage groupMessage) {
        // Fetch the group by ID
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + groupId));

        // Set group reference in the message
         groupMessage.setGroupId(groupId);
        group.getGroupMessages().add(groupMessage);

        groupMessageRepository.save(groupMessage);
        groupRepository.save(group);
        return groupMessage;
    }
}
