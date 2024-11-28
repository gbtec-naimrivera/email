package com.example.email.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Represents an email recipient (To).</p>
 * <p>This class maps to the "email_to" table in the database and holds information about
 * recipients in the "To" field of an email.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "email_to")
public class EmailTo {

    /**
     * <p>The unique ID of the recipient in the "To" field.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "to_id")
    @JsonIgnore
    private Long toId;

    /**
     * <p>Reference to the associated {@link Email} entity.</p>
     * <p>This establishes the relationship between the "To" recipient and the email.</p>
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "email_id", nullable = false)
    private Email email;

    /**
     * <p>The email address of the recipient in the "To" field.</p>
     */
    @Column(name = "email", nullable = false)
    private String emailAddress;

    /**
     * <p>Constructor to create a new {@link EmailTo} object with an email and an email address.</p>
     *
     * @param email The associated email.
     * @param emailAddress The email address of the recipient in the "To" field.
     */
    public EmailTo(Email email, String emailAddress) {
        this.email = email;
        this.emailAddress = emailAddress;
    }
}
