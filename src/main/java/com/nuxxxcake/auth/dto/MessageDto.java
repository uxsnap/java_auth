package com.nuxxxcake.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
  private String message;
  private List<String> errors = new ArrayList<>();

  public MessageDto(String message) {
    this.message = message;
  }
}
