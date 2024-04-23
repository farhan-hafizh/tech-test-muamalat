package com.demo.technicaltestbackend.repositories;

import com.demo.technicaltestbackend.entities.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser, Long> {
}
