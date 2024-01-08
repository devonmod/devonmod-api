package com.devonmod.api.repository;

import com.devonmod.api.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

}
