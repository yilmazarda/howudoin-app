package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail);
}
