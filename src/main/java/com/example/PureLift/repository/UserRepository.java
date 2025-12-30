package com.example.PureLift.repository;

import com.example.PureLift.AppUserRole;
import com.example.PureLift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    long countByAppuserRole(AppUserRole role);
}
