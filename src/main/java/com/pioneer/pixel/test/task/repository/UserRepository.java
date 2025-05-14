package com.pioneer.pixel.test.task.repository;

import com.pioneer.pixel.test.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmailsEmail(String email);

    Optional<User> findByPhonesPhone(String phone);
}