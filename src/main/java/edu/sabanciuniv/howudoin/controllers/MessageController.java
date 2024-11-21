package edu.sabanciuniv.howudoin.controllers;

import edu.sabanciuniv.howudoin.models.Message;
import edu.sabanciuniv.howudoin.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        // Set the sender's email from the authenticated user
        String senderEmail = getAuthenticatedUserEmail();
        message.setSenderEmail(senderEmail);

        messageService.sendMessage(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("")
    public ResponseEntity<List<Message>> getMessages() {
        // Use the authenticated user's email to fetch their messages
        String userEmail = getAuthenticatedUserEmail();
        List<Message> messages = messageService.getMessages(userEmail);
        return ResponseEntity.ok(messages);
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
