package edu.sabanciuniv.howudoin.services;

import edu.sabanciuniv.howudoin.models.Message;
import edu.sabanciuniv.howudoin.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessages(String userEmail, String friendEmail) {
        // Retrieve messages sent and received by the user
        List<Message> messageList = messageRepository.findByReceiverEmailAndSenderEmail(userEmail, friendEmail);
        messageList.addAll(messageRepository.findBySenderEmailAndReceiverEmail(userEmail, friendEmail));

        // Sort the messages by their id in ascending order
        messageList.sort(Comparator.comparing(Message::getId));

        return messageList;
    }
}