package com.example.email.service;

import com.example.email.entity.*;
import com.example.email.facade.EmailFacade;
import com.example.email.repositories.EmailCCDao;
import com.example.email.repositories.EmailDao;
import com.example.email.repositories.EmailToDao;
import com.example.email.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmail() {
        String emailFrom = "test@gbtec.com";
        String emailBody = "This is a test email body";
        int state = 1;

        Email email = Email.builder()
                .emailId(1L)
                .emailFrom(emailFrom)
                .emailBody(emailBody)
                .state(state)
                .build();

        when(emailDao.save(any(Email.class))).thenReturn(email);

        Email createdEmail = emailService.createEmail(emailFrom, emailBody, state, emailToList, emailCCList);

        assertNotNull(createdEmail);
        assertEquals(emailFrom, createdEmail.getEmailFrom());
        assertEquals(emailBody, createdEmail.getEmailBody());
        assertEquals(state, createdEmail.getState());
        verify(emailDao).save(any(Email.class));
    }

    @Test
    void testGetEmailByIdFound() {
        Email email = Email.builder()
                .emailId(1L)
                .emailFrom("test@gbtec.com")
                .emailBody("This is a test email body")
                .state(EmailStateEnum.SENT.getStateCode())
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
        Email email = Email.builder().emailId(1L).build();
        when(emailDao.findById(1L)).thenReturn(Optional.of(email));

        emailService.deleteEmail(1L);

        verify(emailDao).findById(1L);
        verify(emailDao).delete(email);
    }

    @Test
    void testGetAllEmails() {

        Email email1 = new Email();
        email1.setEmailId(1L);
        email1.setEmailFrom("sender1@example.com");
        email1.setEmailBody("Body of email 1");
        email1.setState(EmailStateEnum.DRAFT.getStateCode());

        Email email2 = new Email();
        email2.setEmailId(2L);
        email2.setEmailFrom("sender2@example.com");
        email2.setEmailBody("Body of email 2");
        email2.setState(EmailStateEnum.DRAFT.getStateCode());

        List<Email> emailList = Arrays.asList(email1, email2);

        when(emailDao.findAll()).thenReturn(emailList);

        List<Email> result = emailService.getAllEmails();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("sender1@example.com", result.get(0).getEmailFrom());
        assertEquals("sender2@example.com", result.get(1).getEmailFrom());

        verify(emailDao, times(1)).findAll();
    }


}
