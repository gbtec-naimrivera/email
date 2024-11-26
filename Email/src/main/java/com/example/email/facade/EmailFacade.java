package com.example.email.facade;

import com.example.email.entity.Email;
import com.example.email.entity.EmailCC;
import com.example.email.entity.EmailTo;
import com.example.email.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailFacade {

    @Autowired
    private EmailServiceImpl emailService;

    /**
     * Creates a new email.
     *
     * @param emailFrom         Sender's address.
     * @param emailBody         Email content.
     * @param state             Email state.
     * @param emailToAddresses  List of recipient addresses.
     * @param emailCCAddresses  List of CC addresses.
     * @return Created Email.
     */
    public Email createEmail(String emailFrom, String emailBody, int state, List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {
        return emailService.createEmail(emailFrom, emailBody, state, emailToAddresses, emailCCAddresses);
    }

    /**
     *
     * @param emailsToCreate
     * @return A List of created emails
     */
    public List<Email> createEmails(List<Email> emailsToCreate){
        return emailService.createEmails(emailsToCreate);
    }

    /**
     * Retrieves an email by its ID.
     *
     * @param emailId Email ID.
     * @return Found Email.
     */
    public Email getEmailById(Long emailId) {
        return emailService.getEmailById(emailId);
    }

    /**
     * Updates an existing email.
     *
     * @param emailId           Email ID.
     * @param emailFrom         Sender's address.
     * @param emailBody         Email content.
     * @param state             Email state.
     * @param emailToAddresses  List of recipient addresses.
     * @param emailCCAddresses  List of CC addresses.
     * @return Updated Email.
     */
    public Email updateEmail(Long emailId, String emailFrom, String emailBody, int state, List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {
        return emailService.updateEmail(emailId, emailFrom, emailBody, state, emailToAddresses, emailCCAddresses);
    }


    /**
     * Updates multiple emails.
     *
     * @param emailsToUpdate List of emails to update.
     * @return List of updated emails.
     */
    public List<Email> updateEmails(List<Email> emailsToUpdate) {
        return emailService.updateEmails(emailsToUpdate);
    }

    /**
     * Retrieves all emails.
     *
     * @return List of all emails.
     */
    public List<Email> getAllEmails() {
        return emailService.getAllEmails();
    }

    /**
     * Deletes an email by its ID.
     *
     * @param emailId ID of the email to delete.
     */
    public void deleteEmail(Long emailId) {
        emailService.deleteEmail(emailId);
    }

    /**
     * Deletes multiple emails.
     *
     * @param emailIds List of email IDs to delete.
     */
    public void deleteEmails(List<Long> emailIds) {
        emailService.deleteEmails(emailIds);
    }

    /**
     * Retrieves emails by their state.
     *
     * @param state Email state.
     * @return List of emails with the specified state.
     */
    public List<Email> getEmailsByState(int state) {
        return emailService.getEmailsByState(state);
    }

    /**
     * Marks emails with a specific address as spam.
     */
    public void markEmailsAsSpam() {
        emailService.markEmailsAsSpam();
    }
}
