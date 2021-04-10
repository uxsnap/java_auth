package com.nuxxxcake.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {

  public UserAlreadyExistAuthenticationException(final String msg) {
    super(msg);
  }

}