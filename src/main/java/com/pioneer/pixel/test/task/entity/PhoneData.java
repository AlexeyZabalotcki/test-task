package com.pioneer.pixel.test.task.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 13, unique = true)
    private String phone;

}