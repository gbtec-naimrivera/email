package com.example.email.converter;

import com.example.email.dto.EmailAddressDTO;
import com.example.email.dto.EmailRequestDTO;
import com.example.email.dto.EmailResponseDTO;
import com.example.email.entity.Email;

import java.util.stream.Collectors;

/**
 * <p>Utility class for converting between {@link Email} entities and their corresponding DTOs.</p>
 * <p>This class contains methods to convert {@link Email} to {@link EmailResponseDTO} entities
 */
public class EmailResponseConverter {

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
