package edu.sabanciuniv.howudoin.repositories;

import edu.sabanciuniv.howudoin.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
