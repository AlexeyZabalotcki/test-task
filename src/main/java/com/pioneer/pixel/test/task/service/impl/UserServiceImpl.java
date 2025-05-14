package com.pioneer.pixel.test.task.service.impl;

import com.pioneer.pixel.test.task.dto.AccountDto;
import com.pioneer.pixel.test.task.dto.EmailDto;
import com.pioneer.pixel.test.task.dto.PhoneDto;
import com.pioneer.pixel.test.task.dto.UserDto;
import com.pioneer.pixel.test.task.dto.RegistrationRequest;
import com.pioneer.pixel.test.task.entity.EmailData;
import com.pioneer.pixel.test.task.entity.PhoneData;
import com.pioneer.pixel.test.task.entity.User;
import com.pioneer.pixel.test.task.entity.Account;
import com.pioneer.pixel.test.task.repository.EmailDataRepository;
import com.pioneer.pixel.test.task.repository.PhoneDataRepository;
import com.pioneer.pixel.test.task.repository.UserRepository;
import com.pioneer.pixel.test.task.service.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailDataRepository emailDataRepository;

    private final PhoneDataRepository phoneDataRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public Page<UserDto> searchUsers(LocalDate dateOfBirthAfter, String phone, String email, String name,
                                     Pageable pageable) {
        Specification<User> spec = Specification.where(null);
        if (dateOfBirthAfter != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("dateOfBirth"), dateOfBirthAfter));
        }
        if (StringUtils.hasText(phone)) {
            spec = spec.and((root, query, cb) -> {
                Join<User, PhoneData> phones = root.join("phones", JoinType.INNER);
                return cb.equal(phones.get("phone"), phone);
            });
        }
        if (StringUtils.hasText(email)) {
            spec = spec.and((root, query, cb) -> {
                Join<User, EmailData> emails = root.join("emails", JoinType.INNER);
                return cb.equal(emails.get("email"), email);
            });
        }
        if (StringUtils.hasText(name)) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("name"), name + "%"));
        }
        return userRepository.findAll(spec, pageable)
                .map(this::mapToDto);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public EmailDto addEmail(Long userId, EmailDto emailDto) {
        if (emailDataRepository.findByEmail(emailDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        EmailData emailData = EmailData.builder()
                .email(emailDto.getEmail())
                .user(user)
                .build();
        EmailData saved = emailDataRepository.save(emailData);
        return new EmailDto(saved.getId(), saved.getEmail());
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void deleteEmail(Long userId, Long emailId) {
        EmailData data = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (!data.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot delete email of another user");
        }
        emailDataRepository.delete(data);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public EmailDto updateEmail(Long userId, Long emailId, EmailDto emailDto) {
        Optional<EmailData> existing = emailDataRepository.findByEmail(emailDto.getEmail());
        if (existing.isPresent() && !existing.get().getId().equals(emailId)) {
            throw new IllegalArgumentException("Email already in use");
        }
        EmailData data = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (!data.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot update email of another user");
        }
        data.setEmail(emailDto.getEmail());
        EmailData saved = emailDataRepository.save(data);
        return new EmailDto(saved.getId(), saved.getEmail());
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public PhoneDto addPhone(Long userId, PhoneDto phoneDto) {
        if (phoneDataRepository.findByPhone(phoneDto.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone already in use");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        PhoneData phoneData = PhoneData.builder()
                .phone(phoneDto.getPhone())
                .user(user)
                .build();
        PhoneData saved = phoneDataRepository.save(phoneData);
        return new PhoneDto(saved.getId(), saved.getPhone());
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void deletePhone(Long userId, Long phoneId) {
        PhoneData data = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("Phone not found"));
        if (!data.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot delete phone of another user");
        }
        phoneDataRepository.delete(data);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public PhoneDto updatePhone(Long userId, Long phoneId, PhoneDto phoneDto) {
        Optional<PhoneData> existing = phoneDataRepository.findByPhone(phoneDto.getPhone());
        if (existing.isPresent() && !existing.get().getId().equals(phoneId)) {
            throw new IllegalArgumentException("Phone already in use");
        }
        PhoneData data = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("Phone not found"));
        if (!data.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot update phone of another user");
        }
        data.setPhone(phoneDto.getPhone());
        PhoneData saved = phoneDataRepository.save(data);
        return new PhoneDto(saved.getId(), saved.getPhone());
    }

    @Override
    public UserDto register(RegistrationRequest request) {
        if (emailDataRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (phoneDataRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone already in use");
        }
        User user = User.builder()
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        Account account = Account.builder()
                .user(user)
                .balance(request.getInitialBalance())
                .initialBalance(request.getInitialBalance())
                .build();
        user.setAccount(account);
        EmailData emailData = EmailData.builder()
                .user(user)
                .email(request.getEmail())
                .build();
        user.getEmails().add(emailData);
        PhoneData phoneData = PhoneData.builder()
                .user(user)
                .phone(request.getPhone())
                .build();
        user.getPhones().add(phoneData);
        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setEmails(user.getEmails().stream()
                .map(ed -> {
                    EmailDto e = new EmailDto();
                    e.setId(ed.getId());
                    e.setEmail(ed.getEmail());
                    return e;
                }).collect(Collectors.toList()));
        dto.setPhones(user.getPhones().stream()
                .map(pd -> {
                    PhoneDto p = new PhoneDto();
                    p.setId(pd.getId());
                    p.setPhone(pd.getPhone());
                    return p;
                }).collect(Collectors.toList()));
        AccountDto ad = new AccountDto();
        ad.setBalance(user.getAccount().getBalance());
        ad.setInitialBalance(user.getAccount().getInitialBalance());
        dto.setAccount(ad);
        return dto;
    }
}