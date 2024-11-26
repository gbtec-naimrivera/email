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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing emails.
 * This service handles operations such as creating, updating, deleting, and retrieving emails.
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
     * Creates a new email.
     *
     * @param emailFrom        Sender's email address.
     * @param emailBody        Email body content.
     * @param state            Email state (e.g., 1 = Sent, 2 = Draft).
     * @param emailToAddresses List of recipient email addresses.
     * @param emailCCAddresses List of CC email addresses.
     * @return The created Email object.
     */
    public Email createEmail(String emailFrom, String emailBody, int state, List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {
        Email email = new Email();
        email.setEmailFrom(emailFrom);
        email.setEmailBody(emailBody);
        email.setState(state);
        email = emailDao.save(email);

        Email finalEmail = email;

        List<EmailTo> emailTos = emailToAddresses.stream()
                .map(address -> {
                    EmailTo emailTo = new EmailTo();
                    emailTo.setEmail(finalEmail);
                    emailTo.setEmailAddress(address.getEmailAddress());
                    return emailTo;
                })
                .collect(Collectors.toList());

        emailToDao.saveAll(emailTos);

        List<EmailCC> emailCCs = emailCCAddresses.stream()
                .map(address -> EmailCC.builder()
                        .email(finalEmail)
                        .emailAddress(address.getEmailAddress())
                        .build())
                .collect(Collectors.toList());

        emailCCDao.saveAll(emailCCs);

        email.setEmailTo(emailTos);
        email.setEmailCC(emailCCs);

        return email;
    }

    /**
     * Creates multiple emails in batch.
     *
     * @param emailsToCreate List of Email objects to create.
     * @return List of created Email objects.
     */
    public List<Email> createEmails(List<Email> emailsToCreate) {
        List<Email> createdEmails = new ArrayList<>();

        for (Email emailDetails : emailsToCreate) {
            Email email = new Email();
            email.setEmailFrom(emailDetails.getEmailFrom());
            email.setEmailBody(emailDetails.getEmailBody());
            email.setState(emailDetails.getState());
            email = emailDao.save(email);

            Email finalEmail = email;
            List<EmailTo> emailTos = emailDetails.getEmailTo().stream()
                    .map(address -> {
                        EmailTo emailTo = new EmailTo();
                        emailTo.setEmail(finalEmail);
                        emailTo.setEmailAddress(address.getEmailAddress());
                        return emailTo;
                    })
                    .collect(Collectors.toList());

            emailToDao.saveAll(emailTos);

            Email finalEmail1 = email;
            List<EmailCC> emailCCs = emailDetails.getEmailCC().stream()
                    .map(address -> EmailCC.builder()
                            .email(finalEmail1)
                            .emailAddress(address.getEmailAddress())
                            .build())
                    .collect(Collectors.toList());

            emailCCDao.saveAll(emailCCs);

            email.setEmailTo(emailTos);
            email.setEmailCC(emailCCs);

            createdEmails.add(email);
        }

        return createdEmails;
    }

    /**
     * Retrieves an email by its ID.
     *
     * @param emailId The ID of the email to retrieve.
     * @return The Email object with the given ID.
     * @throws ResourceNotFoundException If the email with the specified ID is not found.
     */
    public Email getEmailById(Long emailId) {
        Optional<Email> email = emailDao.findById(emailId);

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Email with emailId " + emailId + " was not found");
        }

        return email.get();
    }

    /**
     * Updates an existing email.
     *
     * @param emailId           The ID of the email to update.
     * @param emailFrom         Sender's email address.
     * @param emailBody         New body content for the email.
     * @param state             New state for the email.
     * @param emailToAddresses  Updated list of recipient email addresses.
     * @param emailCCAddresses  Updated list of CC email addresses.
     * @return The updated Email object.
     * @throws ResourceNotFoundException If the email with the specified ID is not found.
     * @throws InvalidEmailStateException If the email's state is invalid for updating.
     */
    public Email updateEmail(Long emailId, String emailFrom, String emailBody, int state, List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {
        Email email = emailDao.findById(emailId).orElseThrow(() ->
                new ResourceNotFoundException("Email with emailId " + emailId + " was not found"));

        if (email.getState() != 2) {  // State 2 = Draft
            throw new InvalidEmailStateException("Email state is not valid to update");
        }

        email.setEmailFrom(emailFrom);
        email.setEmailBody(emailBody);
        email.setState(state);

        List<EmailTo> existingTos = email.getEmailTo();
        if (existingTos == null) {
            existingTos = new ArrayList<>();
        }

        for (int i = 0; i < emailToAddresses.size(); i++) {
            if (i < existingTos.size()) {
                existingTos.get(i).setEmailAddress(emailToAddresses.get(i).getEmailAddress());
            } else {
                EmailTo newTo = emailToAddresses.get(i);
                newTo.setEmail(email);
                existingTos.add(newTo);
            }
        }

        emailToDao.saveAll(existingTos);

        List<EmailCC> existingCCs = email.getEmailCC();
        if (existingCCs == null) {
            existingCCs = new ArrayList<>();
        }

        for (int i = 0; i < emailCCAddresses.size(); i++) {
            if (i < existingCCs.size()) {
                existingCCs.get(i).setEmailAddress(emailCCAddresses.get(i).getEmailAddress());
            } else {
                EmailCC newCC = emailCCAddresses.get(i);
                newCC.setEmail(email);
                existingCCs.add(newCC);
            }
        }

        emailCCDao.saveAll(existingCCs);

        email.setEmailTo(existingTos);
        email.setEmailCC(existingCCs);

        return emailDao.save(email);
    }

    /**
     * Updates multiple emails in batch.
     *
     * @param emailsToUpdate List of Email objects to update.
     * @return List of updated Email objects.
     */
    public List<Email> updateEmails(List<Email> emailsToUpdate) {
        List<Email> updatedEmails = new ArrayList<>();

        for (Email emailDetails : emailsToUpdate) {
            try {
                Email updatedEmail = updateEmail(
                        emailDetails.getEmailId(),
                        emailDetails.getEmailFrom(),
                        emailDetails.getEmailBody(),
                        emailDetails.getState(),
                        emailDetails.getEmailTo(),
                        emailDetails.getEmailCC()
                );
                updatedEmails.add(updatedEmail);
            } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException("Email with emailId " + emailDetails.getEmailId() + " was not found");
            } catch (InvalidEmailStateException e) {
                throw new InvalidEmailStateException("Email state is not valid to update for emailId " + emailDetails.getEmailId());
            }
        }

        return updatedEmails;
    }

    /**
     * Retrieves all emails.
     *
     * @return List of all emails.
     */
    @Transactional
    public List<Email> getAllEmails() {
        return emailDao.findAll();
    }

    /**
     * Deletes an email by its ID.
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
     * Deletes multiple emails in batch.
     *
     * @param emailIds List of email IDs to delete.
     */
    public void deleteEmails(List<Long> emailIds) {
        emailDao.deleteAllById(emailIds);
    }

    /**
     * Retrieves emails filtered by their state.
     *
     * @param state The state of the emails to retrieve.
     * @return List of emails with the specified state.
     */
    public List<Email> getEmailsByState(int state) {
        return emailDao.findByState(state);
    }

    /**
     * Marks emails from a specific sender as spam.
     * This is a scheduled task that runs at 10:00 AM every day.
     */
    @Scheduled(cron = "0 0 10 * * ?") // 10:00 AM
    public void markEmailsAsSpam() {
        List<Email> emails = emailDao.findByEmailFrom("carl@gbtec.es");
        for (Email email : emails) {
            email.setState(4);  // Set state to 4 (Spam)
            email.setUpdatedAt(LocalDateTime.now());
        }
        emailDao.saveAll(emails);
    }
}
