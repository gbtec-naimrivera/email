package com.example.email.service;

import com.example.email.entity.*;
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
public class EmailServiceImpl implements EmailService{

    @Autowired
    private EmailDao emailDao;
    @Autowired
    private EmailCCDao emailCCDao;
    @Autowired
    private EmailToDao emailToDao;



    @Override
    public Email createEmail(String emailFrom, String emailBody, int state, List<String> emailToAddresses, List<String> emailCCAddresses) {

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
                    emailTo.setEmailAddress(address);
                    return emailTo;
                })
                .collect(Collectors.toList());
        emailToDao.saveAll(emailTos);

        List<EmailCC> emailCCs = emailCCAddresses.stream()
                .map(address -> EmailCC.builder()
                        .email(finalEmail)
                        .emailAddress(address)
                        .build())
                .collect(Collectors.toList());
        emailCCDao.saveAll(emailCCs);

        email.setEmailTo(emailTos);
        email.setEmailCC(emailCCs);

        return email;
    }
    @Override
    public Email getEmailById(Long emailId) {

        Optional<Email> email = emailDao.findById(emailId);

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Email with emailId " + emailId + " was not found");
        }

        return email.get();
    }

    @Override
    public Email updateEmail(Long emailId, String emailFrom, String emailBody, int state, List<String> emailToAddresses, List<String> emailCCAddresses) {

        // Buscar el email existente
        Email email = emailDao.findById(emailId).orElseThrow(() ->
                new ResourceNotFoundException("Email with emailId " + emailId + " was not found"));

        // Validar el estado del email
        if (email.getState() != 4) {
            throw new InvalidEmailStateException("Email state is not valid to update");
        }

        // Crear nuevas listas de EmailTo y EmailCC sin eliminarlas
        Email finalEmail = email;

        List<EmailTo> emailTos = emailToAddresses.stream()
                .map(address -> {
                    EmailTo emailTo = new EmailTo();
                    emailTo.setEmail(finalEmail); // Establecer la relación bidireccional con el Email
                    emailTo.setEmailAddress(address);
                    return emailTo;
                })
                .collect(Collectors.toList());

        Email finalEmail1 = email;
        List<EmailCC> emailCCs = emailCCAddresses.stream()
                .map(address -> EmailCC.builder()
                        .email(finalEmail1)  // Establecer la relación bidireccional con el Email
                        .emailAddress(address)
                        .build())
                .collect(Collectors.toList());

        // Reemplazar las colecciones de EmailTo y EmailCC con las nuevas listas
        email.setEmailTo(emailTos); // Reemplazar la lista actual de EmailTo
        email.setEmailCC(emailCCs); // Reemplazar la lista actual de EmailCC

        // Actualizar los valores del email
        email.setEmailFrom(emailFrom);
        email.setEmailBody(emailBody);
        email.setState(state);
        email.setUpdatedAt(LocalDateTime.now());

        // Guardar el email actualizado
        email = emailDao.save(email); // Esto también persistirá las nuevas listas de EmailTo y EmailCC

        // Es importante asegurarse de que las entidades de EmailTo y EmailCC se persisten correctamente.
        // Si se hace una operación saveAll, debería gestionarse la persistencia sin errores.
        emailToDao.saveAll(emailTos);
        emailCCDao.saveAll(emailCCs);

        return email;
    }



    @Override
    public List<Email> updateEmails(List<Email> emailsToUpdate) {
        List<Email> updatedEmails = new ArrayList<>();
        for (Email emailDetails : emailsToUpdate) {
            Email email = emailDao.findById(emailDetails.getEmailId())
                    .orElseThrow(() -> new ResourceNotFoundException("Email with emailId " + emailDetails.getEmailId() +
                            " was not found"));
            if (email.getState() == 4) {
                email.setEmailFrom(emailDetails.getEmailFrom());
                email.setEmailBody(emailDetails.getEmailBody());
                email.setEmailTo(emailDetails.getEmailTo());
                email.setEmailCC(emailDetails.getEmailCC());
                email.setUpdatedAt(LocalDateTime.now());
                updatedEmails.add(emailDao.save(email));
            }else{
                throw new InvalidEmailStateException("Email state is not valid to update");
            }
        }
        return updatedEmails;
    }

    @Override
    public List<Email> getAllEmails() {
        return emailDao.findAll();
    }

    @Override
    public void deleteEmail(Long emailId) {
        Email email = emailDao.findById(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("Can not delete because it does not exist an email with emailId" +
                        " :" + " " + emailId));

        emailDao.delete(email);
    }

    @Override
    public void deleteEmails(List<Long> emailIds) {
        emailDao.deleteAllById(emailIds);
    }

    @Override
    public List<Email> getEmailsByState(int state) {
        return emailDao.findByState(state);
    }

    @Override
    @Scheduled(cron = "0 0 10 * * ?") // 10:00 AM
    public void markEmailsAsSpam() {
        List<Email> emails = emailDao.findByEmailFrom("carl@gbtec.es");
        for (Email email : emails) {
            email.setState(3);
            email.setUpdatedAt(LocalDateTime.now());
            emailDao.save(email);
        }
    }
}
