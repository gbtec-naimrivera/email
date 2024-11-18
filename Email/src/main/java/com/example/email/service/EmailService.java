package com.example.email.service;

import com.example.email.entity.Email;

import java.util.List;

public interface EmailService{

    Email createEmail(String emailFrom, String emailBody, int state, List<String> emailToAddresses, List<String> emailCCAddresses);

    Email getEmailById(Long emailId);

    Email updateEmail(Long emailId, String emailFrom, String emailBody, int state, List<String> emailToAddresses, List<String> emailCCAddresses);

    List<Email> updateEmails(List<Email> emailsToUpdate);

    List<Email> getAllEmails();

    void deleteEmail(Long emailId);

    void deleteEmails(List<Long> emailIds);

    List<Email> getEmailsByState(int state);

    void markEmailsAsSpam();
}
