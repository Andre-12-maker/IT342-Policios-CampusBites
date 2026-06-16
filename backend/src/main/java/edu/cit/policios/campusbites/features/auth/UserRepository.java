package edu.cit.policios.campusbites.features.auth;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository; // Import this!
import java.util.Optional;

@Repository // Make sure this is here!
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}