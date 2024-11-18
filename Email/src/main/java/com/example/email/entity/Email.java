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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @Column(name = "email_from", nullable = false)
    private String emailFrom;

    @Column(name = "email_body", nullable = false)
    private String emailBody;

    @Column(name = "state", nullable = false)
    private int state;

    @JsonIgnore
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EmailTo> emailTo;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EmailCC> emailCC;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

}
