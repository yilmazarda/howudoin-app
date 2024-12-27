package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
public interface FriendRequestRepository extends MongoRepository<FriendRequest, ObjectId> {
    List<FriendRequest> findByReceiverEmail(String receiverEmail);
    List<FriendRequest> findBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail);
}
