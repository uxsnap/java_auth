package com.nuxxxcake.auth.controller;

import com.nuxxxcake.auth.dto.*;
import com.nuxxxcake.auth.entity.User;
import com.nuxxxcake.auth.service.AuthService;
import com.nuxxxcake.auth.service.ErrorMessageUnwrapper;
import com.nuxxxcake.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> loginUser(
    @RequestHeader("service_name") String serviceName,
    @Valid @RequestBody LoginDto userDto, Errors errors) {
    JwtResponse jwtResponse = new JwtResponse();

    if (errors.hasErrors()) {
      jwtResponse.setErrors(ErrorMessageUnwrapper.errors(errors));
      return ResponseEntity.badRequest().body(jwtResponse);
    }

    userDto.setServiceName(serviceName);
    jwtResponse = authService.loginUser(userDto);
    if (jwtResponse.getErrors().isEmpty())
      return ResponseEntity.ok(jwtResponse);
    return ResponseEntity.badRequest().body(jwtResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<MessageDto> registerNewUser(
    @RequestHeader("service_name") String serviceName,
    @Valid @RequestBody RegisterDto userDto,
    Errors errors
  ) {
    MessageDto messageDto = new MessageDto();

    if (errors.hasErrors()) {
      messageDto.setErrors(ErrorMessageUnwrapper.errors(errors));
      return ResponseEntity.badRequest().body(messageDto);
    }

    try {
      userDto.setServiceName(serviceName);
      authService.registerNewUserAccount(userDto);
      messageDto.setMessage("New user has been registered");
      return ResponseEntity.ok(messageDto);
    } catch (Exception e) {
      messageDto.setErrors(Collections.singletonList(e.getMessage()));
      return ResponseEntity.badRequest().body(messageDto);
    }
  }

  /* Future functionality */
  @PostMapping("/resetPassword")
  public ResponseEntity<MessageDto> resetPassword(@Valid @RequestBody EmailDto emailDto, Errors errors) {
    if (errors.hasErrors()) {
      MessageDto messageDto = new MessageDto();
      messageDto.setErrors(ErrorMessageUnwrapper.errors(errors));
      return ResponseEntity.badRequest().body(messageDto);
    }

    User user = userService.findByEmail(emailDto.getEmail());
    MessageDto messageDto = new MessageDto();
    if (user == null) {
      messageDto.setErrors(Collections.singletonList("Cannot find user with this email"));
      return ResponseEntity.badRequest().body(messageDto);
    }

    try {
      messageDto.setMessage("Link to reset your password has been sent to your email");
      return ResponseEntity.ok(messageDto);
    } catch (Exception e) {
      messageDto.setErrors(Collections.singletonList(e.getMessage()));
      return ResponseEntity.badRequest().body(messageDto);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageDto> logoutUser() {
    authService.logoutUser();
    return ResponseEntity.ok(new MessageDto("OK"));
  }
}

