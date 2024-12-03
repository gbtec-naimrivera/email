package com.example.email.facade;

import com.example.email.converter.EmailRequestConverter;
import com.example.email.converter.EmailResponseConverter;
import com.example.email.entity.Email;
import com.example.email.dto.EmailRequestDTO;
import com.example.email.dto.EmailResponseDTO;
import com.example.email.entity.EmailStateEnum;
import com.example.email.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.email.converter.EmailRequestConverter.convertToEntity;
import static com.example.email.converter.EmailResponseConverter.convertToResponseDTO;

/**
 * <p>Facade for managing email-related operations.</p>
 * <p>This class handles the communication between the controller and the service layer for email management.</p>
 */
@Component
@RequiredArgsConstructor
public class EmailFacade {

    @Autowired
    private EmailServiceImpl emailService;

    /**
     * <p>Creates a new email.</p>
     *
     * @param emailRequestDTO The details of the email to be created.
     * @return EmailResponseDTO The created email.
     */
    public EmailResponseDTO createEmail(EmailRequestDTO emailRequestDTO) {

        Email email = convertToEntity(emailRequestDTO);

        Email createdEmail = emailService.createEmail(
                email.getEmailFrom(),
                email.getEmailBody(),
                email.getState().getStateCode(),
                email.getEmailTo(),
                email.getEmailCC()
        );

        return convertToResponseDTO(createdEmail);
    }

    /**
     * <p>Creates multiple emails in batch.</p>
     *
     * @param emailsToCreate List of {@link EmailRequestDTO} objects containing the emails to be created.
     * @return List<EmailResponseDTO> The list of created emails.
     */
    public List<EmailResponseDTO> createEmails(List<EmailRequestDTO> emailsToCreate) {
        List<Email> emails = emailsToCreate.stream()
                .map(EmailRequestConverter::convertToEntity)
                .collect(Collectors.toList());

        List<Email> createdEmails = emailService.createEmails(emails);

        return createdEmails.stream()
                .map(EmailResponseConverter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * <p>Retrieves all emails.</p>
     *
     * @return List<EmailResponseDTO> The list of all emails.
     */
    public List<EmailResponseDTO> getAllEmails() {
        List<Email> emails = emailService.getAllEmails();

        return emails.stream()
                .map(EmailResponseConverter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * <p>Retrieves an email by its ID.</p>
     *
     * @param emailId The ID of the email to retrieve.
     * @return EmailResponseDTO The email corresponding to the provided ID.
     */
    public EmailResponseDTO getEmailById(Long emailId) {
        Email email = emailService.getEmailById(emailId);
        return convertToResponseDTO(email);
    }

    /**
     * <p>Retrieves all emails with a specific state.</p>
     *
     * @param state The state of the emails to retrieve.
     * @return List<EmailResponseDTO> The list of emails with the specified state.
     */
    public List<EmailResponseDTO> getEmailsByState(int state) {
        List<Email> emails = emailService.getEmailsByState(EmailStateEnum.fromStateCode(state));

        return emails.stream()
                .map(EmailResponseConverter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * <p>Updates an existing email.</p>
     *
     * @param emailId The ID of the email to update.
     * @param emailRequestDTO The details of the email to be updated.
     * @return EmailResponseDTO The updated email.
     */
    public EmailResponseDTO updateEmail(Long emailId, EmailRequestDTO emailRequestDTO) {
        Email emailToUpdate = convertToEntity(emailRequestDTO);

        Email updatedEmail = emailService.updateEmail(
                emailId,
                emailToUpdate.getEmailFrom(),
                emailToUpdate.getEmailBody(),
                emailToUpdate.getState().getStateCode(),
                emailToUpdate.getEmailTo(),
                emailToUpdate.getEmailCC()
        );

        return convertToResponseDTO(updatedEmail);
    }

    /**
     * <p>Updates multiple emails in batch.</p>
     *
     * @param emailsToUpdate List of {@link EmailRequestDTO} objects containing the emails to be updated.
     * @return List<EmailResponseDTO> The list of updated emails.
     */
    public List<EmailResponseDTO> updateEmails(List<EmailRequestDTO> emailsToUpdate) {
        List<Email> emails = emailsToUpdate.stream()
                .map(EmailRequestConverter::convertToEntity)
                .collect(Collectors.toList());

        List<Email> updatedEmails = emailService.updateEmails(emails);

        return updatedEmails.stream()
                .map(EmailResponseConverter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * <p>Deletes an email by its ID.</p>
     *
     * @param emailId The ID of the email to delete.
     */
    public void deleteEmail(Long emailId) {
        emailService.deleteEmail(emailId);
    }

    /**
     * <p>Deletes multiple emails in batch.</p>
     *
     * @param emailIds List of IDs of the emails to delete.
     */
    public void deleteEmails(List<Long> emailIds) {
        emailService.deleteEmails(emailIds);
    }
}
