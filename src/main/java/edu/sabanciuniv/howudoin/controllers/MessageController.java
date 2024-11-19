package edu.sabanciuniv.howudoin.controllers;

import edu.sabanciuniv.howudoin.models.Message;
import edu.sabanciuniv.howudoin.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<Message>> getMesssages(@RequestBody String userEmail ) {
        List<Message> messages = messageService.getMessages(userEmail);
        return ResponseEntity.ok(messages);
    }
}