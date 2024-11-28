package com.example.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>Data Transfer Object (DTO) for the response containing email details.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResponseDTO {

    /**
     * <p>The ID of the email.</p>
     */
    private Long emailId;

    /**
     * <p>The sender's email address.</p>
     */
    private String emailFrom;

    /**
     * <p>The body content of the email.</p>
     */
    private String emailBody;

    /**
     * <p>The state of the email (e.g., 1 for Sent, 2 for Draft, etc.).</p>
     */
    private int state;

    /**
     * <p>A list of recipients in the "To" field.</p>
     * <p>Each recipient is represented by an {@link EmailAddressDTO} object.</p>
     */
    private List<EmailAddressDTO> emailTo;

    /**
     * <p>A list of recipients in the "CC" field.</p>
     * <p>Each CC recipient is represented by an {@link EmailAddressDTO} object.</p>
     */
    private List<EmailAddressDTO> emailCC;
}
