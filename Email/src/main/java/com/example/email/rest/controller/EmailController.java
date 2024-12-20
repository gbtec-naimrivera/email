package com.example.email.rest.controller;

import com.example.email.facade.EmailFacade;
import com.example.email.dto.EmailRequestDTO;
import com.example.email.dto.EmailResponseDTO;
import com.example.email.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>Contains email-related endpoints.</p>
 */
@RestController
public class EmailController {

    @Autowired
    private EmailFacade emailFacade;

    private RabbitMQProducer producer;
    public EmailController(RabbitMQProducer producer){
        this.producer= producer;
    }

    /**
     * <p>Creates a single email.</p>
     *
     * @param emailRequestDTO The details of the email to be created.
     * @return ResponseEntity<EmailResponseDTO> The created email.
     */
    @PostMapping("/email")
    public ResponseEntity<EmailResponseDTO> createEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        try {
            EmailResponseDTO createdEmail = emailFacade.createEmail(emailRequestDTO);
            producer.sendMessage(String.valueOf(createdEmail.getEmailId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * <p>Creates multiple emails in batch.</p>
     *
     * @param emailRequestDTOs List of {@link EmailRequestDTO} objects containing the emails to be created.
     * @return ResponseEntity<List<EmailResponseDTO>> The list of created emails.
     */
    @PostMapping("/emails")
    public ResponseEntity<List<EmailResponseDTO>> createEmails(@RequestBody List<EmailRequestDTO> emailRequestDTOs) {
        try {
            List<EmailResponseDTO> createdEmails = emailFacade.createEmails(emailRequestDTOs);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * <p>Retrieves all emails.</p>
     *
     * @return ResponseEntity<List<EmailResponseDTO>> List of all stored emails.
     */
    @GetMapping("/emails")
    public ResponseEntity<List<EmailResponseDTO>> getAllEmails() {
        List<EmailResponseDTO> emails = emailFacade.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    /**
     * <p>Retrieves an email by its ID.</p>
     *
     * @param id The ID of the email to retrieve.
     * @return ResponseEntity<EmailResponseDTO> The email corresponding to the provided ID.
     */
    @GetMapping("/email/{id}")
    public ResponseEntity<EmailResponseDTO> getEmailById(@PathVariable Long id) {
        EmailResponseDTO email = emailFacade.getEmailById(id);
        return ResponseEntity.ok(email);
    }

    /**
     * <p>Updates an existing email.</p>
     *
     * @param id The ID of the email to update.
     * @param emailRequestDTO The details of the email to be updated.
     * @return ResponseEntity<EmailResponseDTO> The updated email.
     */
    @PutMapping("/email/{id}")
    public ResponseEntity<EmailResponseDTO> updateEmail(@PathVariable Long id, @RequestBody EmailRequestDTO emailRequestDTO) {
        try {
            EmailResponseDTO updatedEmail = emailFacade.updateEmail(id, emailRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * <p>Updates multiple emails in batch.</p>
     *
     * @param emailRequestDTOs List of {@link EmailRequestDTO} objects containing the emails to be updated.
     * @return ResponseEntity<List<EmailResponseDTO>> The list of updated emails.
     */
    @PutMapping("/emails")
    public ResponseEntity<List<EmailResponseDTO>> updateEmails(@RequestBody List<EmailRequestDTO> emailRequestDTOs) {
        List<EmailResponseDTO> updatedEmails = emailFacade.updateEmails(emailRequestDTOs);
        return ResponseEntity.ok(updatedEmails);
    }

    /**
     * <p>Deletes an email by its ID.</p>
     *
     * @param id The ID of the email to delete.
     * @return ResponseEntity<Void> Response with status 204 (No Content) if deletion is successful.
     */
    @DeleteMapping("/email/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailFacade.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * <p>Deletes multiple emails in batch.</p>
     *
     * @param ids List of IDs of the emails to delete.
     * @return ResponseEntity<Void> Response with status 204 (No Content) if deletion is successful.
     */
    @DeleteMapping("/emails/{ids}")
    public ResponseEntity<Void> deleteEmails(@PathVariable List<Long> ids) {
        emailFacade.deleteEmails(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * <p>Retrieves all emails with a specific state.</p>
     *
     * @param state The state of the emails to retrieve.
     * @return ResponseEntity<List<EmailResponseDTO>> List of emails with the specified state.
     */
    @GetMapping("/emails/state/{state}")
    public ResponseEntity<List<EmailResponseDTO>> getEmailsByState(@PathVariable int state) {
        List<EmailResponseDTO> emails = emailFacade.getEmailsByState(state);
        return ResponseEntity.ok(emails);
    }
}
