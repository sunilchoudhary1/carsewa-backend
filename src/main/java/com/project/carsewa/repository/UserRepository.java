package com.project.carsewa.repository;

import com.project.carsewa.entity.User;
import com.project.carsewa.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);
    User findByRole(UserRole role);
}
