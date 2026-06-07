package com.minimalhealth.repository;

import com.minimalhealth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneOrUsername(String phone, String username);
    boolean existsByPhone(String phone);
}
