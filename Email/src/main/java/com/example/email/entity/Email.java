package com.example.email.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>Represents an email entity.</p>
 * <p>This class maps to the "emails" table in the database and holds the details of an email, including
 * sender, body, state, and recipients (To and CC).</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "emails")
public class Email {

    /**
     * <p>The unique ID of the email.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    /**
     * <p>The sender's email address.</p>
     */
    @Column(name = "email_from", nullable = false)
    private String emailFrom;

    /**
     * <p>The body content of the email.</p>
     */
    @Column(name = "email_body", nullable = false)
    private String emailBody;

    /**
     * <p>The state of the email (e.g., Sent, Draft, etc.).</p>
     */
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailStateEnum state;

    /**
     * <p>The timestamp indicating the last update time of the email.</p>
     */
    @JsonIgnore
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * <p>List of email recipients (To).</p>
     * <p>Each recipient is an instance of {@link EmailTo}.</p>
     */
    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<EmailTo> emailTo;

    /**
     * <p>List of email recipients (CC).</p>
     * <p>Each CC recipient is an instance of {@link EmailCC}.</p>
     */
    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<EmailCC> emailCC;

    /**
     * <p>Updates the "updatedAt" field before persisting or updating the email.</p>
     * <p>This method is automatically called before saving or updating the email in the database.</p>
     */
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
