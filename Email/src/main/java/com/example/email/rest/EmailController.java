package com.example.email.rest;

import com.example.email.entity.Email;
import com.example.email.facade.EmailFacade;
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
    private EmailFacade emailFacade;

    /**
     *
     * @param emailRequest
     */
    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody EmailDto emailRequest) {
        try {
            Email createdEmail = emailFacade.createEmail(
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
        List<Email> emails = emailFacade.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        System.out.println("Buscando...");
        Email email = emailFacade.getEmailById(id);
        return ResponseEntity.ok(email);
    }

    /**
     *
     * @param id
     * @param emailRequest
¡     */
    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody EmailDto emailRequest) {

        try {
            System.out.println("Llega");
            Email emailUpdated = emailFacade.updateEmail(
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

    /**
     *
     * @param emailsToUpdate
¡     */
    @PutMapping("/batch")
    public ResponseEntity<List<Email>> updateEmails(@RequestBody List<Email> emailsToUpdate) {
        List<Email> updatedEmails = emailFacade.updateEmails(emailsToUpdate);
        return ResponseEntity.ok(updatedEmails);
    }

    /**
     *
     * @param id
¡     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailFacade.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * @param emailIds
     * @return
     */
    @DeleteMapping("/batch/{emailIds}")
    public ResponseEntity<Void> deleteEmails(@PathVariable List<Long> emailIds) {
        emailFacade.deleteEmails(emailIds);
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * @param state
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Email>> getEmailsByState(@PathVariable int state) {
        List<Email> emails = emailFacade.getEmailsByState(state);
        return ResponseEntity.ok(emails);
    }
}
