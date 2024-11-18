package com.example.email.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
    private String emailFrom;
    private String emailBody;
    private List<String> emailTo;
    private List<String> emailCC;
    private int state;
}
