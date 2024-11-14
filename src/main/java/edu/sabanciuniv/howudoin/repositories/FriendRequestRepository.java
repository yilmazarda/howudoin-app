package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, Integer> {
}
