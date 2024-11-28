package com.example.email.service;

import com.example.email.entity.*;
import com.example.email.repositories.EmailCCDao;
import com.example.email.repositories.EmailDao;
import com.example.email.repositories.EmailToDao;
import com.example.email.service.exceptions.InvalidEmailStateException;
import com.example.email.service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>Service for managing email-related operations.</p>
 * <p>This service handles tasks such as creating, updating, retrieving, and deleting emails,
 * as well as marking emails as spam.</p>
 */
@Service
@Transactional
public class EmailServiceImpl {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private EmailCCDao emailCCDao;

    @Autowired
    private EmailToDao emailToDao;

    /**
     * <p>Creates a new email.</p>
     *
     * @param emailFrom Sender's email address.
     * @param emailBody The content of the email.
     * @param state The state of the email (e.g., Draft, Sent).
     * @param emailToAddresses List of recipient email addresses.
     * @param emailCCAddresses List of CC email addresses.
     * @return The created email.
     */
    public Email createEmail(String emailFrom, String emailBody, int state,
                             List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {

        Email email = new Email();
        email.setEmailFrom(emailFrom);
        email.setEmailBody(emailBody);
        email.setState(state);

        emailToAddresses.forEach(emailTo -> emailTo.setEmail(email));
        emailCCAddresses.forEach(emailCC -> emailCC.setEmail(email));

        email.setEmailTo(emailToAddresses);
        email.setEmailCC(emailCCAddresses);

        return emailDao.save(email);
    }

    /**
     * <p>Creates multiple emails in batch.</p>
     *
     * @param emailsToCreate List of email objects to create.
     * @return List of created email objects.
     */
    @Transactional
    public List<Email> createEmails(List<Email> emailsToCreate) {
        emailsToCreate.forEach(email -> {
            email.getEmailTo().forEach(emailTo -> emailTo.setEmail(email));
            email.getEmailCC().forEach(emailCC -> emailCC.setEmail(email));
        });

        return emailDao.saveAll(emailsToCreate);
    }

    /**
     * <p>Retrieves an email by its ID.</p>
     *
     * @param emailId The ID of the email to retrieve.
     * @return The email with the specified ID.
     * @throws ResourceNotFoundException If no email is found with the specified ID.
     */
    public Email getEmailById(Long emailId) {
        return emailDao.findById(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("Email with emailId " + emailId + " was not found"));
    }

    /**
     * <p>Updates an existing email.</p>
     *
     * @param emailId The ID of the email to update.
     * @param emailFrom Sender's email address.
     * @param emailBody The content of the email.
     * @param state The state of the email (e.g., Draft, Sent).
     * @param emailToAddresses List of recipient email addresses.
     * @param emailCCAddresses List of CC email addresses.
     * @return The updated email.
     * @throws ResourceNotFoundException If the email with the specified ID is not found.
     * @throws InvalidEmailStateException If the email state is not valid for updating.
     */
    @Transactional
    public Email updateEmail(Long emailId, String emailFrom, String emailBody, int state,
                             List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {

        Email email = emailDao.findById(emailId).orElseThrow(() ->
                new ResourceNotFoundException("Email with emailId " + emailId + " was not found"));

        if (email.getState() != EmailStateEnum.DRAFT.getStateCode()) {
            throw new InvalidEmailStateException("Email state is not valid to update");
        }

        email.setEmailFrom(emailFrom);
        email.setEmailBody(emailBody);
        email.setState(state);

        emailToDao.deleteAll(email.getEmailTo());
        emailCCDao.deleteAll(email.getEmailCC());

        emailToAddresses.forEach(emailTo -> emailTo.setEmail(email));
        emailCCAddresses.forEach(emailCC -> emailCC.setEmail(email));

        email.setEmailTo(emailToAddresses);
        email.setEmailCC(emailCCAddresses);

        return emailDao.save(email);
    }

    /**
     * <p>Updates multiple emails in batch.</p>
     *
     * @param emailsToUpdate List of email objects to update.
     * @return List of updated email objects.
     */
    @Transactional
    public List<Email> updateEmails(List<Email> emailsToUpdate) {
        return emailsToUpdate.stream()
                .map(email -> updateEmail(
                        email.getEmailId(),
                        email.getEmailFrom(),
                        email.getEmailBody(),
                        email.getState(),
                        email.getEmailTo(),
                        email.getEmailCC()
                ))
                .collect(Collectors.toList());
    }

    /**
     * <p>Retrieves all emails.</p>
     *
     * @return List of all emails.
     */
    @Transactional
    public List<Email> getAllEmails() {
        return emailDao.findAll();
    }

    /**
     * <p>Deletes an email by its ID.</p>
     *
     * @param emailId The ID of the email to delete.
     */
    public void deleteEmail(Long emailId) {
        Optional<Email> emailOptional = emailDao.findById(emailId);
        if (emailOptional.isPresent()) {
            Email email = emailOptional.get();
            emailDao.delete(email);
        }
    }

    /**
     * <p>Deletes multiple emails in batch.</p>
     *
     * @param emailIds List of email IDs to delete.
     */
    public void deleteEmails(List<Long> emailIds) {
        emailDao.deleteAllById(emailIds);
    }

    /**
     * <p>Retrieves emails filtered by their state.</p>
     *
     * @param state The state of the emails to retrieve.
     * @return List of emails with the specified state.
     */
    public List<Email> getEmailsByState(int state) {
        return emailDao.findByState(state);
    }

    /**
     * <p>Marks emails from a specific sender as spam.</p>
     * <p>This is a scheduled task that runs at 10:00 AM every day.</p>
     */
    @Scheduled(cron = "0 0 10 * * ?") // 10:00 AM
    public void markEmailsAsSpam() {
        List<Email> emails = emailDao.findByEmailFrom("carl@gbtec.es");
        emails.forEach(email -> {
            email.setState(EmailStateEnum.SPAM.getStateCode());
            email.setUpdatedAt(LocalDateTime.now());
        });
        emailDao.saveAll(emails);
    }
}
