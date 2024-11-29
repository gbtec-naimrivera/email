package com.example.email.repositories;

import com.example.email.entity.Email;
import com.example.email.entity.EmailStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>Repository interface for managing {@link Email} entities.</p>
 * <p>This interface extends {@link JpaRepository} to provide CRUD operations for the {@link Email} entity.</p>
 */
public interface EmailDao extends JpaRepository<Email, Long> {

    /**
     * <p>Finds a list of emails by their state.</p>
     *
     * @param state The state of the emails to retrieve (e.g., 1 for Sent, 2 for Draft).
     * @return A list of emails with the specified state.
     */
    List<Email> findByState(EmailStateEnum state);

    /**
     * <p>Finds a list of emails by the sender's email address.</p>
     *
     * @param emailFrom The email address of the sender.
     * @return A list of emails sent from the specified email address.
     */
    List<Email> findByEmailFrom(String emailFrom);

}
