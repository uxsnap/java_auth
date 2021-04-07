package com.nuxxxcake.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PasswordDto {
  @NotBlank(message = "Password must not be empty")
  @Size(min = 8, max = 255, message = "The length of password must be more than 8 and less than 255")
  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,255}$",
    message = "Password must include at least 1 uppercase letter, 1 digit and 1 lowercase letter"
  ) // Password regexp
  protected String password;
}
