package com.example.email.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Represents an email CC recipient.</p>
 * <p>This class maps to the "email_cc" table in the database and holds information about
 * recipients in the CC field of an email.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "email_cc")
public class EmailCC {

    /**
     * <p>The unique ID of the CC recipient.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cc_id")
    @JsonIgnore
    private Long ccId;

    /**
     * <p>Reference to the associated {@link Email} entity.</p>
     * <p>This establishes the relationship between the CC recipient and the email.</p>
     */
    @ManyToOne
    @JoinColumn(name = "email_id", nullable = false)
    @JsonBackReference
    private Email email;

    /**
     * <p>The email address of the CC recipient.</p>
     */
    @Column(name = "email", nullable = false)
    private String emailAddress;

    /**
     * <p>Constructor to create a new {@link EmailCC} object with an email and an email address.</p>
     *
     * @param email The associated email.
     * @param emailAddress The email address of the CC recipient.
     */
    public EmailCC(Email email, String emailAddress) {
        this.email = email;
        this.emailAddress = emailAddress;
    }
}
