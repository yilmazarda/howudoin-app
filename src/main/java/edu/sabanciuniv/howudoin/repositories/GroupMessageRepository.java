package edu.sabanciuniv.howudoin.repositories;


import edu.sabanciuniv.howudoin.models.GroupMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupMessageRepository extends MongoRepository<GroupMessage, String> {
}
