package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderEmail(String email);
    List<Message> findByReceiverEmail(String email);
    List<Message> findBySenderEmailAndReceiverEmail(String sender, String receiver);
    List<Message> findByReceiverEmailAndSenderEmail(String receiver, String sender);
}