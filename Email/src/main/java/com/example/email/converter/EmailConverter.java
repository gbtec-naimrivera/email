package com.example.email.converter;

import com.example.email.entity.Email;
import com.example.email.entity.EmailCC;
import com.example.email.entity.EmailStateEnum;
import com.example.email.entity.EmailTo;
import com.example.email.dto.EmailRequestDTO;
import com.example.email.dto.EmailResponseDTO;
import com.example.email.dto.EmailAddressDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Utility class for converting between {@link Email} entities and their corresponding DTOs.</p>
 * <p>This class contains methods to convert {@link EmailRequestDTO} to {@link Email} entities
 * and {@link Email} entities to {@link EmailResponseDTO}.</p>
 */
public class EmailConverter {

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

    /**
     * <p>Converts an {@link Email} entity to an {@link EmailResponseDTO}.</p>
     *
     * @param email The {@link Email} entity to convert.
     * @return The corresponding {@link EmailResponseDTO}.
     */
    public static EmailResponseDTO convertToResponseDTO(Email email) {
        EmailResponseDTO responseDTO = new EmailResponseDTO();
        responseDTO.setEmailId(email.getEmailId());
        responseDTO.setEmailFrom(email.getEmailFrom());
        responseDTO.setEmailBody(email.getEmailBody());
        responseDTO.setState(email.getState().getStateCode());

        responseDTO.setEmailTo(email.getEmailTo().stream()
                .map(emailTo -> new EmailAddressDTO(emailTo.getEmailAddress()))
                .collect(Collectors.toList()));

        responseDTO.setEmailCC(email.getEmailCC().stream()
                .map(emailCC -> new EmailAddressDTO(emailCC.getEmailAddress()))
                .collect(Collectors.toList()));

        return responseDTO;
    }
}
