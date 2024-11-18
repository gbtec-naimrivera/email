package com.example.email.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "email_to")
public class EmailTo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "to_id")
    @JsonIgnore
    private Long toId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "email_id", nullable = false)
    private Email email;

    @Column(name = "email", nullable = false)
    private String emailAddress;
}
