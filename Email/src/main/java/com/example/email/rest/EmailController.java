package com.example.email.rest;

import com.example.email.entity.Email;
import com.example.email.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/emails")
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody EmailDto emailRequest) {
        try {
            Email createdEmail = emailService.createEmail(
                    emailRequest.getEmailFrom(),
                    emailRequest.getEmailBody(),
                    emailRequest.getState(),
                    emailRequest.getEmailTo(),
                    emailRequest.getEmailCC()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        List<Email> emails = emailService.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        System.out.println("Buscando...");
        Email email = emailService.getEmailById(id);
        return ResponseEntity.ok(email);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody EmailDto emailRequest) {

        try {
            System.out.println("Llega");
            Email emailUpdated = emailService.updateEmail(
                    id,
                    emailRequest.getEmailFrom(),
                    emailRequest.getEmailBody(),
                    emailRequest.getState(),
                    emailRequest.getEmailTo(),
                    emailRequest.getEmailCC());
            return ResponseEntity.status(HttpStatus.CREATED).body(emailUpdated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/batch")
    public ResponseEntity<List<Email>> updateEmails(@RequestBody List<Email> emailsToUpdate) {
        List<Email> updatedEmails = emailService.updateEmails(emailsToUpdate);
        return ResponseEntity.ok(updatedEmails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteEmails(@RequestBody List<Long> emailIds) {
        emailService.deleteEmails(emailIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Email>> getEmailsByState(@PathVariable int state) {
        List<Email> emails = emailService.getEmailsByState(state);
        return ResponseEntity.ok(emails);
    }
}
