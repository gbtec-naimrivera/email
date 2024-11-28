package com.example.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object (DTO) for representing an email address.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailAddressDTO {

    /**
     * <p>The email address represented by this DTO.</p>
     */
    private String email;
}
