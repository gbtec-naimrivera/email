package com.example.email.service;

import com.example.email.entity.*;
import com.example.email.service.exceptions.InvalidEmailStateException;
import com.example.email.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailDao emailDao;
    @Autowired
    private EmailToDao emailToDao;
    @Autowired
    private EmailCCDao emailCCDao;

    List<String> emailToList = Arrays.asList("recipient1@gbtec.com", "recipient2@gbtec.com");
    List<String> emailCCList = Arrays.asList("cc1@gbtec.com", "cc2@gbtec.com");

    @Test
    void testCreateEmail() {
        Email email = new Email();
        email.setEmailFrom("test@gbtec.com");
        email.setEmailBody("This is a test email body");
        email.setState(4);

        emailDao.save(email);

        Email createdEmail = emailService.createEmail(
                email.getEmailFrom(),
                email.getEmailBody(),
                email.getState(),
                emailToList,
                emailCCList
        );

        assertNotNull(createdEmail);
        assertEquals("test@gbtec.com", createdEmail.getEmailFrom());
        assertEquals("This is a test email body", createdEmail.getEmailBody());
        assertEquals(4, createdEmail.getState());

        assertNotNull(createdEmail.getEmailTo());
        assertNotNull(createdEmail.getEmailCC());
        assertEquals(emailToList.size(), createdEmail.getEmailTo().size());
        assertEquals(emailCCList.size(), createdEmail.getEmailCC().size());
    }

    @Test
    void testGetEmailByIdFound() {
        Email email = new Email();
        email.setEmailFrom("test@gbtec.com");
        email.setEmailBody("This is a test email body");
        email.setState(4);

        emailDao.save(email);

        Email foundEmail = emailService.getEmailById(email.getEmailId());

        assertNotNull(foundEmail);
        assertEquals(email.getEmailId(), foundEmail.getEmailId());
        assertEquals("test@gbtec.com", foundEmail.getEmailFrom());
    }

    @Test
    void testGetEmailByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            emailService.getEmailById(999L);
        });
    }

    @Test
    void testUpdateEmailSuccessful() {
        Email email = new Email();
        email.setEmailFrom("test@gbtec.com");
        email.setEmailBody("This is a test email body");
        email.setState(4);

        emailDao.save(email);

        email.setEmailFrom("updated@gbtec.com");
        email.setEmailBody("Updated email body");

        Email updatedEmail = emailService.updateEmail(email.getEmailId(), email);

        assertNotNull(updatedEmail);
        assertEquals("updated@gbtec.com", updatedEmail.getEmailFrom());
        assertEquals("Updated email body", updatedEmail.getEmailBody());
    }

    @Test
    void testUpdateEmailNotInDraft() {
        Email email = new Email();
        email.setEmailFrom("test@gbtec.com");
        email.setEmailBody("This is a test email body");
        email.setState(3);

        emailDao.save(email);

        email.setState(3);

        InvalidEmailStateException thrown = assertThrows(InvalidEmailStateException.class, () -> {
            emailService.updateEmail(email.getEmailId(), email);
        });
    }

    @Test
    void testUpdateEmails() {
        Email email1 = new Email();
        email1.setEmailFrom("test1@gbtec.com");
        email1.setEmailBody("This is email 1");
        email1.setState(4);

        Email email2 = new Email();
        email2.setEmailFrom("test2@gbtec.com");
        email2.setEmailBody("This is email 2");
        email2.setState(4);

        emailDao.save(email1);
        emailDao.save(email2);

        Email emailUpdate1 = new Email();
        emailUpdate1.setEmailFrom("updated1@gbtec.com");
        emailUpdate1.setEmailBody("Updated email body");
        emailUpdate1.setState(4);
        emailDao.save(emailUpdate1);

        Email emailUpdate2 = new Email();
        emailUpdate2.setEmailFrom("updated2@gbtec.com");
        emailUpdate2.setEmailBody("Updated email body");
        emailUpdate2.setState(4);
        emailDao.save(emailUpdate2);

        List<Email> emailsToUpdate = new ArrayList<>();
        emailsToUpdate.add(emailUpdate1);
        emailsToUpdate.add(emailUpdate2);

        List<Email> updatedEmails = emailService.updateEmails(emailsToUpdate);

        assertNotNull(updatedEmails);
        assertEquals(2, updatedEmails.size());

        Email updatedEmail1 = updatedEmails.get(0);
        assertEquals("updated1@gbtec.com", updatedEmail1.getEmailFrom());
        assertEquals("Updated email body", updatedEmail1.getEmailBody());

        Email updatedEmail2 = updatedEmails.get(1);
        assertEquals("updated2@gbtec.com", updatedEmail2.getEmailFrom());
        assertEquals("Updated email body", updatedEmail2.getEmailBody());
    }

    @Test
    void testUpdateEmailsWithInvalidState() {
        Email email = new Email();
        email.setEmailFrom("test1@gbtec.com");
        email.setEmailBody("This is email 1");
        email.setState(2);
        emailDao.save(email);

        assertThrows(InvalidEmailStateException.class, () -> {
            emailService.updateEmails(Arrays.asList(email));
        });
    }

    @Test
    void testDeleteEmailSuccessful() {
        Email email = new Email();
        email.setEmailFrom("test@gbtec.com");
        email.setEmailBody("This is a test email body");
        email.setState(4);

        emailDao.save(email);

        emailService.deleteEmail(email.getEmailId());

        assertFalse(emailDao.existsById(1L));
    }

    @Test
    void testDeleteEmailNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            emailService.deleteEmail(999L);
        });
    }

    @Test
    void testGetEmailsByState() {
        Email email = new Email();
        email.setEmailFrom("test@gbtec.com");
        email.setEmailBody("This is a test email body");
        email.setState(4);
        emailDao.save(email);

        List<Email> emails = emailService.getEmailsByState(4);

        assertNotNull(emails);
        assertEquals(1, emails.size());
    }
}
