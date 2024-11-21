package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
