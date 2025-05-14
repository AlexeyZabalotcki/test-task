package com.pioneer.pixel.test.task.service;

import com.pioneer.pixel.test.task.dto.EmailDto;
import com.pioneer.pixel.test.task.dto.PhoneDto;
import com.pioneer.pixel.test.task.dto.UserDto;
import com.pioneer.pixel.test.task.dto.RegistrationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {

    Page<UserDto> searchUsers(LocalDate dateOfBirthAfter, String phone, String email, String name, Pageable pageable);

    EmailDto addEmail(Long userId, EmailDto emailDto);

    void deleteEmail(Long userId, Long emailId);

    EmailDto updateEmail(Long userId, Long emailId, EmailDto emailDto);

    PhoneDto addPhone(Long userId, PhoneDto phoneDto);

    void deletePhone(Long userId, Long phoneId);

    PhoneDto updatePhone(Long userId, Long phoneId, PhoneDto phoneDto);

    UserDto register(RegistrationRequest request);
}