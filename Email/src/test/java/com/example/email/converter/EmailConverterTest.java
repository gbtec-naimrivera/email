package com.example.email.converter;

import com.example.email.dto.EmailAddressDTO;
import com.example.email.dto.EmailRequestDTO;
import com.example.email.dto.EmailResponseDTO;
import com.example.email.entity.Email;
import com.example.email.entity.EmailCC;
import com.example.email.entity.EmailStateEnum;
import com.example.email.entity.EmailTo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static com.example.email.converter.EmailRequestConverter.convertToEntity;
import static com.example.email.converter.EmailResponseConverter.convertToResponseDTO;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmailConverterTest {

    @Mock
    private EmailRequestDTO emailRequestDTO;



    @Test
    public void testConvertToEntity() {

        when(emailRequestDTO.getEmailId()).thenReturn(1L);
        when(emailRequestDTO.getEmailFrom()).thenReturn("test@gbtec.com");
        when(emailRequestDTO.getEmailBody()).thenReturn("This is the email body.");
        when(emailRequestDTO.getState()).thenReturn(1);
        when(emailRequestDTO.getEmailTo()).thenReturn(Arrays.asList(new EmailAddressDTO("to1@gbtec.com"), new EmailAddressDTO("to2@gbtec.com")));
        when(emailRequestDTO.getEmailCC()).thenReturn(List.of(new EmailAddressDTO("cc1@gbtec.com")));

        Email result = convertToEntity(emailRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getEmailId());
        assertEquals("test@gbtec.com", result.getEmailFrom());
        assertEquals("This is the email body.", result.getEmailBody());
        assertEquals(EmailStateEnum.SENT, result.getState());

        assertEquals(2, result.getEmailTo().size());
        assertEquals(1, result.getEmailCC().size());
    }

    @Test
    public void testConvertToResponseDTO() {

        Email email = mock(Email.class);
        when(email.getEmailId()).thenReturn(1L);
        when(email.getEmailFrom()).thenReturn("test@gbtec.com");
        when(email.getEmailBody()).thenReturn("This is the email body.");
        when(email.getState()).thenReturn(EmailStateEnum.SENT);

        EmailTo emailTo1 = mock(EmailTo.class);
        EmailTo emailTo2 = mock(EmailTo.class);
        when(emailTo1.getEmailAddress()).thenReturn("to1@gbtec.com");
        when(emailTo2.getEmailAddress()).thenReturn("to2@gbtec.com");
        List<EmailTo> emailTos = Arrays.asList(emailTo1, emailTo2);
        when(email.getEmailTo()).thenReturn(emailTos);

        EmailCC emailCC1 = mock(EmailCC.class);
        EmailCC emailCC2 = mock(EmailCC.class);
        when(emailCC1.getEmailAddress()).thenReturn("cc1@gbtec.com");
        when(emailCC2.getEmailAddress()).thenReturn("cc2@gbtec.com");
        List<EmailCC> emailCCs = Arrays.asList(emailCC1, emailCC2);
        when(email.getEmailCC()).thenReturn(emailCCs);

        EmailResponseDTO responseDTO = convertToResponseDTO(email);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getEmailId().longValue());
        assertEquals("test@gbtec.com", responseDTO.getEmailFrom());
        assertEquals("This is the email body.", responseDTO.getEmailBody());
        assertEquals(EmailStateEnum.SENT.getStateCode(), responseDTO.getState());

        assertNotNull(responseDTO.getEmailTo());
        assertEquals(2, responseDTO.getEmailTo().size());
        assertEquals("to1@gbtec.com", responseDTO.getEmailTo().get(0).getEmail());
        assertEquals("to2@gbtec.com", responseDTO.getEmailTo().get(1).getEmail());

        assertNotNull(responseDTO.getEmailCC());
        assertEquals(2, responseDTO.getEmailCC().size());
        assertEquals("cc1@gbtec.com", responseDTO.getEmailCC().get(0).getEmail());
        assertEquals("cc2@gbtec.com", responseDTO.getEmailCC().get(1).getEmail());
    }


}
