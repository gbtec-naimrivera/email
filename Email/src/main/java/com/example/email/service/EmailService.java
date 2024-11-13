package com.example.email.service;

import com.example.email.entity.Email;
import com.example.email.entity.EmailDao;
import com.example.email.service.exceptions.InvalidEmailStateException;
import com.example.email.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private EmailDao emailDao;

    public Email createEmail(Email email) {
        email.setUpdatedAt(LocalDateTime.now());

        return emailDao.save(email);
    }
    // Obtener un correo electrónico por ID
    public Email getEmailById(Long emailId) {
        Optional<Email> email = emailDao.findById(emailId);

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Email con ID " + emailId + " no encontrado");
        }

        return email.get();
    }

    // Actualizar un correo electrónico
    public Email updateEmail(Long emailId, Email emailDetails) {
        Email email = emailDao.findById(emailId).orElseThrow(() ->
                new ResourceNotFoundException("Email con Id " + emailId + " no encontrado"));

        if (! "BORRADOR".equals(email.getState())) {
            throw new InvalidEmailStateException("Solo los emails de borradores pueden actualizarse");
        }

        email.setEmailFrom(emailDetails.getEmailFrom());
        email.setEmailBody(emailDetails.getEmailBody());
        email.setEmailTo(emailDetails.getEmailTo());
        email.setEmailCC(emailDetails.getEmailCC());
        email.setUpdatedAt(LocalDateTime.now());
        return emailDao.save(email);
    }

    // Actualizar múltiples correos electrónicos
    public List<Email> updateEmails(List<Email> emailsToUpdate) {
        List<Email> updatedEmails = new ArrayList<>();
        for (Email emailDetails : emailsToUpdate) {
            Email email = emailDao.findById(emailDetails.getEmailId())
                    .orElseThrow(() -> new ResourceNotFoundException("Email con Id " + emailDetails.getEmailId() +
                            " no encontrado"));
            if ("BORRADOR".equals(email.getState())) {
                email.setEmailFrom(emailDetails.getEmailFrom());
                email.setEmailBody(emailDetails.getEmailBody());
                email.setEmailTo(emailDetails.getEmailTo());
                email.setEmailCC(emailDetails.getEmailCC());
                email.setUpdatedAt(LocalDateTime.now());
                updatedEmails.add(emailDao.save(email));
            }
        }
        return updatedEmails;
    }

    // Obtener todos los correos electrónicos
    public List<Email> getAllEmails() {
        return emailDao.findAll();
    }

    // Eliminar un correo electrónico por ID
    public void deleteEmail(Long emailId) {
        Email email = emailDao.findById(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede borrar el correo porque no existe con ID:"
                        + " " + emailId));

        emailDao.delete(email);
    }

    // Eliminar múltiples correos electrónicos por lista de IDs
    public void deleteEmails(List<Long> emailIds) {
        emailDao.deleteAllById(emailIds);
    }

    // Obtener correos electrónicos por estado
    public List<Email> getEmailsByState(String state) {
        return emailDao.findByState(state);
    }

    // Marcar correos electrónicos como spam (ejemplo programado)
    @Scheduled(cron = "0 0 10 * * ?") // 10:00 AM
    public void markEmailsAsSpam() {
        List<Email> emails = emailDao.findByEmailFrom("carl@gbtec.es");
        for (Email email : emails) {
            email.setState("SPAM");
            email.setUpdatedAt(LocalDateTime.now());
            emailDao.save(email);
        }
    }
}
