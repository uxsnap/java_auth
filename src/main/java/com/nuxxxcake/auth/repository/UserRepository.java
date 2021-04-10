package com.nuxxxcake.auth.repository;

import com.nuxxxcake.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByEmail(String email);

  User findByUsernameOrEmail(String name, String email);
}
