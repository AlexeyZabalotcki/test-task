package com.pioneer.pixel.test.task.repository;

import com.pioneer.pixel.test.task.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    Optional<PhoneData> findByPhone(String phone);
}