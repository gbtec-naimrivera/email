package com.example.email.service;

import com.example.email.entity.*;
import com.example.email.service.exceptions.InvalidEmailStateException;
import com.example.email.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmailServiceTest {

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private EmailDao emailDao;
    @Autowired
    private EmailToDao emailToDao;
    @Autowired
    private EmailCCDao emailCCDao;

    List<String> emailToList = Arrays.asList("recipient1@gbtec.com", "recipient2@gbtec.com");
    List<String> emailCCList = Arrays.asList("cc1@gbtec.com", "cc2@gbtec.com");

    List<String> emailToListUpdated = Arrays.asList("recipient1updated@gbtec.com", "recipient2updated@gbtec.com");
    List<String> emailCCListUpdated = Arrays.asList("cc1updated@gbtec.com", "cc2updated@gbtec.com");

    @BeforeEach
    public void cleanUp() {
        emailToDao.deleteAll();
        emailCCDao.deleteAll();
        emailDao.deleteAll();
    }


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

        Email createdEmail = emailService.createEmail(
                email.getEmailFrom(),
                email.getEmailBody(),
                email.getState(),
                emailToList,
                emailCCList
        );

        String emailFrom = "test2@gbtec.com";
        String emailBody = "This is an updated body";
        int state = 1;
        List<String> emailToList2 = Arrays.asList("recipient1@gbtec.com", "recipient2@gbtec.com");
        List<String> emailCCList2 = Arrays.asList("cc1@gbtec.com", "cc2@gbtec.com");

        emailService.updateEmail(createdEmail.getEmailId(), emailFrom, emailBody, state, emailToList2, emailToList2);

        assertEquals(createdEmail.getEmailBody(), emailBody);
        assertEquals(createdEmail.getEmailFrom(), emailFrom);
        assertEquals(createdEmail.getState(), state);
        assertEquals(emailToList2.size(), createdEmail.getEmailTo().size());
        assertEquals(emailCCList2.size(), createdEmail.getEmailCC().size());

    }


    @Test
    void testUpdateMultipleEmails() {

        Email email1 = new Email();
        email1.setEmailFrom("test@gbtec.com");
        email1.setEmailBody("This is a test email body");
        email1.setState(4);
        emailDao.save(email1);

        EmailTo emailto = new EmailTo();
        emailto.setEmail(email1);
        emailto.setEmailAddress("recipient1@gbtec.com");
        emailToDao.save(emailto);

        EmailTo emailto2 = new EmailTo();
        emailto2.setEmail(email1);
        emailto2.setEmailAddress("recipient2@gbtec.com");
        emailToDao.save(emailto2);

        EmailCC emailCC = new EmailCC();
        emailCC.setEmail(email1);
        emailCC.setEmailAddress("cc1@gbtec.com");
        emailCCDao.save(emailCC);

        EmailCC emailCC2 = new EmailCC();
        emailCC2.setEmail(email1);
        emailCC2.setEmailAddress("cc12@gbtec.com");
        emailCCDao.save(emailCC2);

        List<EmailTo> emailToList = new ArrayList<>();
        emailToList.add(emailto);
        emailToList.add(emailto2);

        List<EmailCC> emailCCList = new ArrayList<>();
        emailCCList.add(emailCC);
        emailCCList.add(emailCC2);

        email1.setEmailTo(emailToList);
        email1.setEmailCC(emailCCList);

        Email email2 = new Email();
        email2.setEmailFrom("test2@gbtec.com");
        email2.setEmailBody("This is email 2 body");
        email2.setState(4);
        emailDao.save(email2);

        EmailTo emailto3 = new EmailTo();
        emailto3.setEmail(email2);
        emailto3.setEmailAddress("recipient3@gbtec.com");
        emailToDao.save(emailto3);

        EmailTo emailto4 = new EmailTo();
        emailto4.setEmail(email2);
        emailto4.setEmailAddress("recipient4@gbtec.com");
        emailToDao.save(emailto4);

        EmailCC emailCC3 = new EmailCC();
        emailCC3.setEmail(email2);
        emailCC3.setEmailAddress("cc3@gbtec.com");
        emailCCDao.save(emailCC3);

        EmailCC emailCC4 = new EmailCC();
        emailCC4.setEmail(email2);
        emailCC4.setEmailAddress("cc13@gbtec.com");
        emailCCDao.save(emailCC4);

        List<EmailTo> emailToList2 = new ArrayList<>();
        emailToList2.add(emailto3);
        emailToList2.add(emailto4);

        List<EmailCC> emailCCList2 = new ArrayList<>();
        emailCCList2.add(emailCC3);
        emailCCList2.add(emailCC4);

        email2.setEmailTo(emailToList2);
        email2.setEmailCC(emailCCList2);

        Email emailUpdate1 = new Email();
        emailUpdate1.setEmailFrom("update1@gbtec.com");
        emailUpdate1.setEmailBody("This is email update 1 body");
        emailUpdate1.setState(4);
        emailDao.save(emailUpdate1);

        EmailTo emailto5 = new EmailTo();
        emailto5.setEmail(emailUpdate1);
        emailto5.setEmailAddress("recipient5@gbtec.com");
        emailToDao.save(emailto5);

        EmailTo emailto6 = new EmailTo();
        emailto6.setEmail(emailUpdate1);
        emailto6.setEmailAddress("recipient6@gbtec.com");
        emailToDao.save(emailto6);

        EmailCC emailCC5 = new EmailCC();
        emailCC5.setEmail(emailUpdate1);
        emailCC5.setEmailAddress("cc5@gbtec.com");
        emailCCDao.save(emailCC5);

        EmailCC emailCC6 = new EmailCC();
        emailCC6.setEmail(emailUpdate1);
        emailCC6.setEmailAddress("cc14@gbtec.com");
        emailCCDao.save(emailCC6);

        List<EmailTo> emailToList3 = new ArrayList<>();
        emailToList3.add(emailto5);
        emailToList3.add(emailto6);

        List<EmailCC> emailCCList3 = new ArrayList<>();
        emailCCList3.add(emailCC5);
        emailCCList3.add(emailCC6);

        emailUpdate1.setEmailTo(emailToList3);
        emailUpdate1.setEmailCC(emailCCList3);

        Email emailUpdate2 = new Email();
        emailUpdate2.setEmailFrom("update2@gbtec.com");
        emailUpdate2.setEmailBody("This is email update 2 body");
        emailUpdate2.setState(4);
        emailDao.save(emailUpdate2);

        EmailTo emailto7 = new EmailTo();
        emailto7.setEmail(emailUpdate2);
        emailto7.setEmailAddress("recipient7@gbtec.com");
        emailToDao.save(emailto7);

        EmailTo emailto8 = new EmailTo();
        emailto8.setEmail(emailUpdate2);
        emailto8.setEmailAddress("recipient8@gbtec.com");
        emailToDao.save(emailto8);

        EmailCC emailCC7 = new EmailCC();
        emailCC7.setEmail(emailUpdate2);
        emailCC7.setEmailAddress("cc7@gbtec.com");
        emailCCDao.save(emailCC7);

        EmailCC emailCC8 = new EmailCC();
        emailCC8.setEmail(emailUpdate2);
        emailCC8.setEmailAddress("cc15@gbtec.com");
        emailCCDao.save(emailCC8);

        List<EmailTo> emailToList4 = new ArrayList<>();
        emailToList4.add(emailto7);
        emailToList4.add(emailto8);

        List<EmailCC> emailCCList4 = new ArrayList<>();
        emailCCList4.add(emailCC7);
        emailCCList4.add(emailCC8);

        emailUpdate2.setEmailTo(emailToList4);
        emailUpdate2.setEmailCC(emailCCList4);

        List<Email> emailsToUpdate = Arrays.asList(emailUpdate1, emailUpdate2);

        List<Email> updatedEmails = emailService.updateEmails(emailsToUpdate);

        assertNotNull(updatedEmails);
        assertEquals(2, updatedEmails.size());

        Email updatedEmail1 = updatedEmails.stream()
                .filter(email -> email.getEmailId().equals(emailUpdate1.getEmailId()))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedEmail1);
        assertEquals("update1@gbtec.com", updatedEmail1.getEmailFrom());
        assertEquals("This is email update 1 body", updatedEmail1.getEmailBody());

        Email updatedEmail2 = updatedEmails.stream()
                .filter(email -> email.getEmailId().equals(emailUpdate2.getEmailId()))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedEmail2);
        assertEquals("update2@gbtec.com", updatedEmail2.getEmailFrom());
        assertEquals("This is email update 2 body", updatedEmail2.getEmailBody());
    }




    /*@Test
    void testUpdateEmailsWithInvalidState() {
        Email email = new Email();
        email.setEmailFrom("test1@gbtec.com");
        email.setEmailBody("This is email 1");
        email.setState(2);
        emailDao.save(email);

        assertThrows(InvalidEmailStateException.class, () -> {
            emailService.updateEmails(Arrays.asList(email));
        });
    }*/

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
