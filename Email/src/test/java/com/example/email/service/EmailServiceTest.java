package com.example.email.service;

import com.example.email.entity.*;
import com.example.email.repositories.EmailCCDao;
import com.example.email.repositories.EmailDao;
import com.example.email.repositories.EmailToDao;
import com.example.email.service.exceptions.InvalidEmailStateException;
import com.example.email.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailDao emailDao;

    @Mock
    private EmailToDao emailToDao;

    @Mock
    private EmailCCDao emailCCDao;

    @InjectMocks
    private EmailServiceImpl emailService;

    private final List<EmailTo> emailToList = Arrays.asList(
            new EmailTo(null, null, "recipient1@gbtec.com"),
            new EmailTo(null, null, "recipient2@gbtec.com")
    );

    private final List<EmailCC> emailCCList = Arrays.asList(
            new EmailCC(null, null, "cc1@gbtec.com"),
            new EmailCC(null, null, "cc2@gbtec.com")
    );

    @Test
    void testCreateEmail() {
        String emailFrom = "test@gbtec.com";
        String emailBody = "This is a test email body";
        int state = 1;

        Email email = Email.builder()
                .emailId(1L)
                .emailFrom(emailFrom)
                .emailBody(emailBody)
                .state(EmailStateEnum.fromStateCode(1))
                .build();

        when(emailDao.save(any(Email.class))).thenReturn(email);

        Email createdEmail = emailService.createEmail(email);

        assertNotNull(createdEmail);
        assertEquals(emailFrom, createdEmail.getEmailFrom());
        assertEquals(emailBody, createdEmail.getEmailBody());
        assertEquals(state, createdEmail.getState().getStateCode());
        verify(emailDao).save(any(Email.class));
    }

    @Test
    void testCreateEmails() {

        Email email1 = Email.builder()
                .emailId(1L)
                .emailFrom("sender1@gbtec.com")
                .emailBody("Body of email 1")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();

        Email email2 = Email.builder()
                .emailId(2L)
                .emailFrom("sender2@gbtec.com")
                .emailBody("Body of email 2")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();

        List<Email> emails = Arrays.asList(email1, email2);

        when(emailDao.saveAll(anyList())).thenReturn(emails);

        List<Email> createdEmails = emailService.createEmails(emails);

        assertNotNull(createdEmails);
        assertEquals(2, createdEmails.size());
        assertEquals("sender1@gbtec.com", createdEmails.get(0).getEmailFrom());
        assertEquals("sender2@gbtec.com", createdEmails.get(1).getEmailFrom());

        verify(emailDao).saveAll(anyList());
    }

    @Test
    void testGetEmailByIdFound() {
        Email email = Email.builder()
                .emailId(1L)
                .emailFrom("test@gbtec.com")
                .emailBody("This is a test email body")
                .state(EmailStateEnum.SENT)
                .build();

        when(emailDao.findById(1L)).thenReturn(Optional.of(email));

        Email foundEmail = emailService.getEmailById(1L);

        assertNotNull(foundEmail);
        assertEquals(email.getEmailId(), foundEmail.getEmailId());
        assertEquals(email.getEmailFrom(), foundEmail.getEmailFrom());
        verify(emailDao).findById(1L);

    }

    @Test
    void testGetEmailByIdNotFound() {

        when(emailDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            emailService.getEmailById(999L);
        });

        verify(emailDao).findById(999L);
    }

    @Test
    void testDeleteEmailSuccessful() {

        emailService.deleteEmail(1L);

        verify(emailDao).deleteById(1L);
    }

    @Test
    void testDeleteEmailNotFound() {

        emailService.deleteEmail(999L);

        verify(emailDao).deleteById(999L);

    }

    @Test
    void testGetAllEmails() {

        Email email1 = new Email();
        email1.setEmailId(1L);
        email1.setEmailFrom("sender1@example.com");
        email1.setEmailBody("Body of email 1");
        email1.setState(EmailStateEnum.DRAFT);

        Email email2 = new Email();
        email2.setEmailId(2L);
        email2.setEmailFrom("sender2@example.com");
        email2.setEmailBody("Body of email 2");
        email2.setState(EmailStateEnum.DRAFT);

        List<Email> emailList = Arrays.asList(email1, email2);

        when(emailDao.findAll()).thenReturn(emailList);

        List<Email> result = emailService.getAllEmails();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("sender1@example.com", result.get(0).getEmailFrom());
        assertEquals("sender2@example.com", result.get(1).getEmailFrom());

        verify(emailDao, times(1)).findAll();
    }

    @Test
    void testUpdateEmailSuccessful() {
        Email existingEmail = Email.builder()
                .emailId(1L)
                .emailFrom("test@gbtec.com")
                .emailBody("Test email body")
                .state(EmailStateEnum.DRAFT)
                .build();


        when(emailDao.findById(1L)).thenReturn(Optional.of(existingEmail));
        when(emailDao.save(any(Email.class))).thenReturn(existingEmail);

        Email updatedEmail = emailService.updateEmail(1L, "newSender@gbtec.com",
                "Updated email body", EmailStateEnum.DRAFT.getStateCode(), emailToList, emailCCList);

        assertNotNull(updatedEmail);
        assertEquals("newSender@gbtec.com", updatedEmail.getEmailFrom());
        assertEquals("Updated email body", updatedEmail.getEmailBody());
        assertEquals(EmailStateEnum.DRAFT.getStateCode(), updatedEmail.getState().getStateCode());

        verify(emailDao).findById(1L);
        verify(emailDao).save(any(Email.class));
    }

    @Test
    void testUpdateEmailResourceNotFoundException() {
        Long emailId = 999L;

        when(emailDao.findById(emailId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            emailService.updateEmail(emailId, "newSender@gbtec.com", "Updated email body", 1, emailToList, emailCCList);
        });

        verify(emailDao).findById(emailId);
        verify(emailDao, never()).save(any(Email.class));
    }

    @Test
    void testUpdateEmailInvalidStateException() {
        Long emailId = 1L;

        Email email = Email.builder()
                .emailId(emailId)
                .emailFrom("oldSender@gbtec.com")
                .emailBody("Original body")
                .state(EmailStateEnum.SENT)
                .build();

        when(emailDao.findById(emailId)).thenReturn(Optional.of(email));

        assertThrows(InvalidEmailStateException.class, () -> {
            emailService.updateEmail(emailId, "newSender@gbtec.com", "Updated email body", 1, emailToList, emailCCList);
        });

        verify(emailDao).findById(emailId);
        verify(emailDao, never()).save(any(Email.class));
    }

    @Test
    void testUpdateEmails() {

        Email email1 = Email.builder()
                .emailId(1L)
                .emailFrom("sender1@gbtec.com")
                .emailBody("Original body email 1")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();

        Email email2 = Email.builder()
                .emailId(2L)
                .emailFrom("sender2@gbtec.com")
                .emailBody("Original body email 2")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();

        Email email1updated = Email.builder()
                .emailId(1L)
                .emailFrom("sender1@gbtec.com")
                .emailBody("Update body email 1")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();

        Email email2updated = Email.builder()
                .emailId(2L)
                .emailFrom("sender2@gbtec.com")
                .emailBody("Updated body email 2")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();


        when(emailDao.save(email1)).thenReturn(email1);
        when(emailDao.save(email2)).thenReturn(email2);
        when(emailDao.findById(1L)).thenReturn(Optional.of(email1));
        when(emailDao.findById(2L)).thenReturn(Optional.of(email2));

        List<Email> emailsList = Arrays.asList(email1updated, email2updated);

        List<Email> emailsUpdated = emailService.updateEmails(emailsList);

        assertNotNull(emailsUpdated);
        assertEquals(emailsUpdated.get(0), email1updated);
        assertEquals(emailsUpdated.get(1), email2updated);

    }

    @Test
    void testGetEmailsByState() {

        Email email1 = Email.builder()
                .emailId(1L)
                .emailFrom("sender1@gbtec.com")
                .emailBody("Email body 1")
                .state(EmailStateEnum.DRAFT)
                .build();

        Email email2 = Email.builder()
                .emailId(2L)
                .emailFrom("sender2@gbtec.com")
                .emailBody("Email body 2")
                .state(EmailStateEnum.DRAFT)
                .build();

        List<Email> emails = Arrays.asList(email1, email2);

        when(emailDao.findByState(EmailStateEnum.DRAFT)).thenReturn(emails);

        List<Email> result = emailService.getEmailsByState(EmailStateEnum.DRAFT);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(EmailStateEnum.DRAFT.getStateCode(), result.get(0).getState().getStateCode());
        assertEquals(EmailStateEnum.DRAFT.getStateCode(), result.get(1).getState().getStateCode());

        verify(emailDao).findByState(EmailStateEnum.DRAFT);
    }

    @Test
    void testDeleteEmails() {

        Email email1 = Email.builder().emailId(1L).build();
        Email email2 = Email.builder().emailId(2L).build();

        List<Long> emailIds = Arrays.asList(1L, 2L);

        emailService.deleteEmails(emailIds);

        verify(emailDao).deleteAllById(emailIds);
    }

    @Test
    void testMarkEmailsAsSpam() {

        List<Email> emails = Arrays.asList(
                Email.builder()
                        .emailId(1L)
                        .emailFrom("carl@gbtec.com")
                        .state(EmailStateEnum.DRAFT)
                        .build(),
                Email.builder()
                        .emailId(2L)
                        .emailFrom("carl@gbtec.com")
                        .state(EmailStateEnum.SENT)
                        .build()
        );

        when(emailDao.findByEmailFrom("carl@gbtec.com")).thenReturn(emails);

        emailService.markEmailsAsSpam();

        assertEquals(EmailStateEnum.SPAM, emails.get(0).getState());
        assertEquals(EmailStateEnum.SPAM, emails.get(1).getState());
        assertNotNull(emails.get(0).getUpdatedAt());
        assertNotNull(emails.get(1).getUpdatedAt());
    }

}





