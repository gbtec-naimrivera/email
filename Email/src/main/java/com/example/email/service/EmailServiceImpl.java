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

@Service
@Transactional
public class EmailServiceImpl{

    @Autowired
    private EmailDao emailDao;
    @Autowired
    private EmailCCDao emailCCDao;
    @Autowired
    private EmailToDao emailToDao;

    /**
     *
     * @param emailFrom
     * @param emailBody
     * @param state
     * @param emailToAddresses
     * @param emailCCAddresses
     * @return
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
     *
     * @param emailId
     * @return
     */
    public Email getEmailById(Long emailId) {

        Optional<Email> email = emailDao.findById(emailId);

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Email with emailId " + emailId + " was not found");
        }

        return email.get();
    }

    /**
     *
     * @param emailId
     * @param emailFrom
     * @param emailBody
     * @param state
     * @param emailToAddresses
     * @param emailCCAddresses
     * @return
     */
    public Email updateEmail(Long emailId, String emailFrom, String emailBody, int state, List<EmailTo> emailToAddresses, List<EmailCC> emailCCAddresses) {
        Email email = emailDao.findById(emailId).orElseThrow(() ->
                new ResourceNotFoundException("Email with emailId " + emailId + " was not found"));

        if (email.getState() != 2) {
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
     *
     * @param emailsToUpdate
     * @return
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
     *
     * @return
     */
    @Transactional
    public List<Email> getAllEmails() {
        return emailDao.findAll();
    }

    /**
     *
     * @param emailId
     */
    public void deleteEmail(Long emailId) {
        Optional<Email> emailOptional = emailDao.findById(emailId);
        if (emailOptional.isPresent()) {
            Email email = emailOptional.get();
            emailDao.delete(email);
        }
    }

    /**
     *
     * @param emailIds
     */
    public void deleteEmails(List<Long> emailIds) {
        emailDao.deleteAllById(emailIds);
    }

    /**
     *
     * @param state
     * @return
     */
    public List<Email> getEmailsByState(int state) {
        return emailDao.findByState(state);
    }

    @Scheduled(cron = "0 0 10 * * ?") // 10:00 AM
    public void markEmailsAsSpam() {
        List<Email> emails = emailDao.findByEmailFrom("carl@gbtec.es");
        for (Email email : emails) {
            email.setState(4);
            email.setUpdatedAt(LocalDateTime.now());
        }
        emailDao.saveAll(emails);
    }
}
