package com.pioneer.pixel.test.task.service;

import com.pioneer.pixel.test.task.entity.User;
import com.pioneer.pixel.test.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (username.contains("@")) {
            user = userRepository.findByEmailsEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        } else {
            user = userRepository.findByPhonesPhone(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Phone not found"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(), user.getPassword(), Collections.emptyList());
    }
}