package Rest;

import Entity.Email;
import Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Crear un nuevo email
    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody Email email) {
        try {
            Email createdEmail = emailService.createEmail(email);
            return new ResponseEntity<>(createdEmail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los emails
    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        List<Email> emails = emailService.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    // Obtener un email por ID
    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        Email email = emailService.getEmailById(id);
        return ResponseEntity.ok(email);
    }


    // Actualizar un email por ID
    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody Email emailDetails) {
        Email updatedEmail = emailService.updateEmail(id, emailDetails);
        return ResponseEntity.ok(updatedEmail);
    }

    // Actualizar múltiples emails
    @PutMapping("/batch")
    public ResponseEntity<List<Email>> updateEmails(@RequestBody List<Email> emailsToUpdate) {
        List<Email> updatedEmails = emailService.updateEmails(emailsToUpdate);
        return ResponseEntity.ok(updatedEmails);
    }

    // Eliminar un email por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar múltiples emails por sus IDs
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteEmails(@RequestBody List<Long> emailIds) {
        emailService.deleteEmails(emailIds);
        return ResponseEntity.noContent().build();
    }

    // Obtener emails por estado
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Email>> getEmailsByState(@PathVariable String state) {
        List<Email> emails = emailService.getEmailsByState(state);
        return ResponseEntity.ok(emails);
    }
}
