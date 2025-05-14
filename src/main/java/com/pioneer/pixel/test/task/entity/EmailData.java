package com.pioneer.pixel.test.task.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200, unique = true)
    private String email;

}