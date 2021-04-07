package com.nuxxxcake.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailMessageDto {
  private String subject;
  private String text;
  private String to;
  private String from;
}