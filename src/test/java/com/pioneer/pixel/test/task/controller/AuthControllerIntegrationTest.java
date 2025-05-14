package com.pioneer.pixel.test.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pioneer.pixel.test.task.entity.Account;
import com.pioneer.pixel.test.task.entity.EmailData;
import com.pioneer.pixel.test.task.entity.PhoneData;
import com.pioneer.pixel.test.task.entity.User;
import com.pioneer.pixel.test.task.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testtask")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = User.builder()
                .name("Test User")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .password(passwordEncoder.encode("password"))
                .build();

        Account account = Account.builder()
                .user(user)
                .balance(java.math.BigDecimal.valueOf(100))
                .initialBalance(java.math.BigDecimal.valueOf(100))
                .build();
        user.setAccount(account);

        EmailData emailData = EmailData.builder()
                .user(user)
                .email("test@example.com")
                .build();
        user.getEmails().add(emailData);

        PhoneData phoneData = PhoneData.builder()
                .user(user)
                .phone("79207865432")
                .build();
        user.getPhones().add(phoneData);

        userRepository.save(user);
    }

    @Test
    void login_successful() throws Exception {
        var request = Collections.singletonMap("username", "test@example.com");
        request = new java.util.HashMap<>() {
            {
                put("username", "test@example.com");
                put("password", "password");
            }
        };

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}