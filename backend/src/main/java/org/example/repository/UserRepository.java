package org.example.repository;
import java.util.Optional;
import org.example.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
