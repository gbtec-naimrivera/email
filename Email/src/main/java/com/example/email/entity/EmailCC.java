package com.example.email.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "email_cc")
public class EmailCC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cc_id")
    private Long ccId;

    @ManyToOne
    @JoinColumn(name = "email_id", nullable = false)
    private Email email;

    @Column(name = "email", nullable = false)
    private String emailAddress;
}
