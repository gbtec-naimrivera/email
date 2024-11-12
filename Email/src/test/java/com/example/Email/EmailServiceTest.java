package com.example.Email;

import Entity.Email;
import Entity.EmailDao;
import Service.EmailService;
import Service.Exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private EmailDao emailDao;

    private Email email;
    private Email emailToUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        email = new Email();
        email.setEmailId(1L);
        email.setEmailFrom("test@example.com");
        email.setEmailBody("Test email body");
        email.setState("BORRADOR");
        email.setUpdatedAt(LocalDateTime.now());
        email.setEmailTo(new ArrayList<>(Arrays.asList("dest1@example.com", "dest2@example.com")));
        email.setEmailCC(new ArrayList<>(Arrays.asList("dest3@example.com")));

        emailToUpdate = new Email();
        emailToUpdate.setEmailId(1L);
        emailToUpdate.setEmailFrom("updated@example.com");
        emailToUpdate.setEmailBody("Updated email body");
        emailToUpdate.setState("BORRADOR");
        emailToUpdate.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void saveEmailTest() {
        when(emailDao.save(any(Email.class))).thenReturn(email);

        Email savedEmail = emailService.createEmail(email);
        assertNotNull(savedEmail);
        assertEquals("test@example.com", savedEmail.getEmailFrom());
        verify(emailDao, times(1)).save(any(Email.class));
    }

    @Test
    void getEmailByIdTest() {
        when(emailDao.findById(1L)).thenReturn(Optional.of(email));

        Optional<Email> foundEmail = Optional.ofNullable(emailService.getEmailById(1L));
        assertTrue(foundEmail.isPresent());
        assertEquals("test@example.com", foundEmail.get().getEmailFrom());
    }

    @Test
    void getEmailByIdNotFoundTest() {
        when(emailDao.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailService.getEmailById(1L);
        });

        assertEquals("Email con ID 1 no encontrado", exception.getMessage());
    }

    @Test
    void updateEmailTest() {
        when(emailDao.findById(1L)).thenReturn(Optional.of(email));
        when(emailDao.save(any(Email.class))).thenReturn(emailToUpdate);

        Email updatedEmail = emailService.updateEmail(1L, emailToUpdate);
        assertEquals("updated@example.com", updatedEmail.getEmailFrom());
        assertEquals("Updated email body", updatedEmail.getEmailBody());
    }

    @Test
    void updateEmailWithInvalidStateTest() {
        email.setState("ENVIADO");

        when(emailDao.findById(1L)).thenReturn(Optional.of(email));

        InvalidEmailStateException exception = assertThrows(InvalidEmailStateException.class, () -> {
            emailService.updateEmail(1L, emailToUpdate);
        });

    }

    @Test
    void updateEmailsTest() {
        List<Email> emailList = Arrays.asList(email, emailToUpdate);
        when(emailDao.findById(anyLong())).thenReturn(Optional.of(email));
        when(emailDao.save(any(Email.class))).thenReturn(emailToUpdate);

        List<Email> updatedEmails = emailService.updateEmails(emailList);

        assertEquals(2, updatedEmails.size());
        verify(emailDao, times(2)).save(any(Email.class));
    }

    @Test
    void deleteEmailsTest() {
        List<Long> emailIds = Arrays.asList(1L, 2L, 3L);
        doNothing().when(emailDao).deleteAllById(emailIds);

        emailService.deleteEmails(emailIds);
        verify(emailDao, times(1)).deleteAllById(emailIds);
    }

    // Este test es ahora innecesario, ya que no se usa EmailDestinatarios
    @Test
    void addEmailToDestinatariosTest() {
        // Eliminado ya que no es necesario con la nueva estructura
    }

    @Test
    void getEmailsByStateTest() {
        when(emailDao.findByState("BORRADOR")).thenReturn(Arrays.asList(email));

        List<Email> emails = emailService.getEmailsByState("BORRADOR");
        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals("test@example.com", emails.get(0).getEmailFrom());
    }

    @Test
    void markEmailsAsSpamTest() {
        List<Email> emails = new ArrayList<>();
        Email email1 = new Email();
        email1.setEmailFrom("carl@gbtec.es");
        email1.setState("BORRADOR");
        email1.setEmailBody("Mensaje de prueba 1");
        emails.add(email1);

        Email email2 = new Email();
        email2.setEmailFrom("carl@gbtec.es");
        email2.setState("ENVIADO");
        email2.setEmailBody("Mensaje de prueba 2");
        emails.add(email2);

        when(emailDao.findByEmailFrom("carl@gbtec.es")).thenReturn(emails);

        emailService.markEmailsAsSpam();

        for (Email email : emails) {
            assertEquals("SPAM", email.getState());
            verify(emailDao, times(1)).save(email);
        }
    }
}
