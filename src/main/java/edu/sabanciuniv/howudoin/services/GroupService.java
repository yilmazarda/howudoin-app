package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.Group;
import edu.sabanciuniv.howudoin.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAllGroups()
    {
        return groupRepository.findAll();
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    public Group getGroupById (Integer groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);

        if(group == null) {
            throw new RuntimeException("User not found");
        }

        return group;
    }

    public void addMemberToGroup(Integer groupId, String userEmail) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null) {
            throw new RuntimeException("User not found");
        }
        group.getUsers().add(userEmail);

        groupRepository.save(group);
    }

    public List<String> getMembers(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null) {
            throw new RuntimeException("User not found");
        }

        return group.getUsers();
    }



    //Message class is not yet implemented
    /*
    public List<Message> getMessages(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null) {
            throw new RuntimeException("Group not found");
        }

        return group.getMessages();
    }

     public Message sendMessage(Integer groupId, Message message) {
        // Fetch the group by ID
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + groupId));

        // Set group reference in the message
        message.setGroup(group);

        // Save the message (assuming you have a MessageRepository)
        return messageRepository.save(message);
    }
    */
}
