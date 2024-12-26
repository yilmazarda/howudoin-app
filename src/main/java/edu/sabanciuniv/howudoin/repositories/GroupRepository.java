package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findByUsersContains(String email);
}
