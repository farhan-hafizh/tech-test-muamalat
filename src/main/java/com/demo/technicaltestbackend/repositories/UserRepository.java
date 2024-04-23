package com.demo.technicaltestbackend.repositories;

import com.demo.technicaltestbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT * FROM users u WHERE u.username = ?1")
    User findByUsername(String username);
}
