package com.pioneer.pixel.test.task.config;

import com.pioneer.pixel.test.task.entity.Account;
import com.pioneer.pixel.test.task.entity.EmailData;
import com.pioneer.pixel.test.task.entity.PhoneData;
import com.pioneer.pixel.test.task.entity.User;
import com.pioneer.pixel.test.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user = User.builder()
                    .name("Default User")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .password(passwordEncoder.encode("password"))
                    .build();

            Account account = Account.builder()
                    .user(user)
                    .balance(BigDecimal.valueOf(1000))
                    .initialBalance(BigDecimal.valueOf(1000))
                    .build();
            user.setAccount(account);

            EmailData email = EmailData.builder()
                    .user(user)
                    .email("default@example.com")
                    .build();
            user.getEmails().add(email);

            PhoneData phone = PhoneData.builder()
                    .user(user)
                    .phone("79200000000")
                    .build();
            user.getPhones().add(phone);

            userRepository.save(user);
        }
    }
}