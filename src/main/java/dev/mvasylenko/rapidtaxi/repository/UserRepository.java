package dev.mvasylenko.rapidtaxi.repository;

import dev.mvasylenko.rapidtaxi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
