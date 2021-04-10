package com.nuxxxcake.auth.service;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ErrorMessageUnwrapper {
  public static List<String> errors(Errors errors) {
    return  errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
      .collect(Collectors.toList());
  }
}
