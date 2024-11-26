package com.example.email.rest.controller;

import com.example.email.entity.Email;
import com.example.email.facade.EmailFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing email operations.
 * This controller exposes endpoints for creating, retrieving, updating, and deleting emails.
 * It also allows filtering emails by their state.
 */
@RestController
@RequestMapping(value = "/emails")
public class EmailController {

    @Autowired
    private EmailFacade emailFacade;

    /**
     * Creates a single email.
     *
     * @param emailRequest The JSON object containing the email details to be created.
     *                     It must include the following fields:
     *                     - emailFrom: Sender's email address.
     *                     - emailBody: Body of the email.
     *                     - state: The email's state (1 = Sent, 2 = Draft, etc.).
     *                     - emailTo: List of recipient email addresses.
     *                     - emailCC: List of CC email addresses.
     * @return ResponseEntity<Email> The created email.
     *         Response with status 201 if the creation is successful, or 400 if the data is incorrect.
     */
    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody Email emailRequest) {
        try {
            Email createdEmail = emailFacade.createEmail(
                    emailRequest.getEmailFrom(),
                    emailRequest.getEmailBody(),
                    emailRequest.getState(),
                    emailRequest.getEmailTo(),
                    emailRequest.getEmailCC()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates multiple emails in batch.
     *
     * @param emailsToCreate List of {@link Email} objects containing the emails to be created.
     * @return ResponseEntity<List<Email>> List of created emails.
     *         Response with status 201 if the creation is successful, or 500 if an error occurs.
     */
    @PostMapping("/batch")
    public ResponseEntity<List<Email>> createEmails(@RequestBody List<Email> emailsToCreate) {
        try {
            List<Email> createdEmails = emailFacade.createEmails(emailsToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all emails.
     *
     * @return ResponseEntity<List<Email>> List of all stored emails.
     *         Response with status 200 (OK) if retrieval is successful.
     */
    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        List<Email> emails = emailFacade.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    /**
     * Retrieves an email by its ID.
     *
     * @param id The ID of the email to retrieve.
     * @return ResponseEntity<Email> The email corresponding to the provided ID.
     *         Response with status 200 (OK) if the email is found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        Email email = emailFacade.getEmailById(id);
        return ResponseEntity.ok(email);
    }

    /**
     * Updates an existing email.
     *
     * @param id The ID of the email to update.
     * @param emailRequest The JSON object containing the new details for the email.
     * @return ResponseEntity<Email> The updated email.
     *         Response with status 201 (CREATED) if the update is successful.
     *         Response with status 500 if an unexpected error occurs.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody Email emailRequest) {
        try {
            Email updatedEmail = emailFacade.updateEmail(
                    id,
                    emailRequest.getEmailFrom(),
                    emailRequest.getEmailBody(),
                    emailRequest.getState(),
                    emailRequest.getEmailTo(),
                    emailRequest.getEmailCC()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates multiple emails in batch.
     *
     * @param emailsToUpdate List of {@link Email} objects containing the emails to update.
     * @return ResponseEntity<List<Email>> List of updated emails.
     *         Response with status 200 (OK) if the update is successful.
     */
    @PutMapping("/batch")
    public ResponseEntity<List<Email>> updateEmails(@RequestBody List<Email> emailsToUpdate) {
        List<Email> updatedEmails = emailFacade.updateEmails(emailsToUpdate);
        return ResponseEntity.ok(updatedEmails);
    }

    /**
     * Deletes an email by its ID.
     *
     * @param id The ID of the email to delete.
     * @return ResponseEntity<Void> Response with status 204 (No Content) if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailFacade.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes multiple emails in batch.
     *
     * @param emailIds List of IDs of the emails to delete.
     * @return ResponseEntity<Void> Response with status 204 (No Content) if deletion is successful.
     */
    @DeleteMapping("/batch/{emailIds}")
    public ResponseEntity<Void> deleteEmails(@PathVariable List<Long> emailIds) {
        emailFacade.deleteEmails(emailIds);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all emails with a specific state.
     *
     * @param state The state of the emails to retrieve (e.g., 1 for Sent, 2 for Draft).
     * @return ResponseEntity<List<Email>> List of emails with the specified state.
     *         Response with status 200 (OK) if retrieval is successful.
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Email>> getEmailsByState(@PathVariable int state) {
        List<Email> emails = emailFacade.getEmailsByState(state);
        return ResponseEntity.ok(emails);
    }
}
