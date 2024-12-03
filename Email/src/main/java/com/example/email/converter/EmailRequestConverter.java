package com.example.email.converter;

import com.example.email.entity.Email;
import com.example.email.entity.EmailCC;
import com.example.email.entity.EmailStateEnum;
import com.example.email.entity.EmailTo;
import com.example.email.dto.EmailRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Utility class for converting between {@link Email} entities and their corresponding DTOs.</p>
 * <p>This class contains methods to convert {@link EmailRequestDTO} to {@link Email} entities
 */
public class EmailRequestConverter {

    /**
     * <p>Converts a {@link EmailRequestDTO} to an {@link Email} entity.</p>
     *
     * @param emailRequestDTO The {@link EmailRequestDTO} object to convert.
     * @return The corresponding {@link Email} entity.
     */
    public static Email convertToEntity(EmailRequestDTO emailRequestDTO) {
        Email email = new Email();
        email.setEmailId(emailRequestDTO.getEmailId());
        email.setEmailFrom(emailRequestDTO.getEmailFrom());
        email.setEmailBody(emailRequestDTO.getEmailBody());
        email.setState(EmailStateEnum.fromStateCode(emailRequestDTO.getState()));

        List<EmailTo> emailTos = emailRequestDTO.getEmailTo().stream()
                .map(dto -> new EmailTo(email, dto.getEmail()))
                .collect(Collectors.toList());

        List<EmailCC> emailCCs = emailRequestDTO.getEmailCC().stream()
                .map(dto -> new EmailCC(email, dto.getEmail()))
                .collect(Collectors.toList());

        email.setEmailTo(emailTos);
        email.setEmailCC(emailCCs);

        return email;
    }
}
