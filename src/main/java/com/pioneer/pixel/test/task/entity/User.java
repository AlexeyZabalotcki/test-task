package com.pioneer.pixel.test.task.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Account account;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailData> emails = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneData> phones = new ArrayList<>();

    public void addEmail(EmailData emailData) {
        emails.add(emailData);
        emailData.setUser(this);
    }

    public void removeEmail(EmailData emailData) {
        emails.remove(emailData);
        emailData.setUser(null);
    }

    public void addPhone(PhoneData phoneData) {
        phones.add(phoneData);
        phoneData.setUser(this);
    }

    public void removePhone(PhoneData phoneData) {
        phones.remove(phoneData);
        phoneData.setUser(null);
    }

}