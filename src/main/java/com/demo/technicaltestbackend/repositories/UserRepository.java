package com.demo.technicaltestbackend.repositories;

import com.demo.technicaltestbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
