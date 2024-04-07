package com.favour.Horizon.Banking.App.repository;

import com.favour.Horizon.Banking.App.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    UserEntity findByAccountNumber(String accountNumber);

    Optional<UserEntity> findByEmail(String email);

}
