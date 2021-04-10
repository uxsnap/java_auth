package com.nuxxxcake.auth.service;

import com.nuxxxcake.auth.dto.JwtResponse;
import com.nuxxxcake.auth.dto.LoginDto;
import com.nuxxxcake.auth.dto.MailMessageDto;
import com.nuxxxcake.auth.dto.RegisterDto;
import com.nuxxxcake.auth.entity.PasswordResetToken;
import com.nuxxxcake.auth.entity.User;
import com.nuxxxcake.auth.exceptions.UserAlreadyExistAuthenticationException;
import com.nuxxxcake.auth.repository.PasswordResetTokenRepository;
import com.nuxxxcake.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Value("${spring.custom.secret_salt_key}")
  public String SECRET_KEY;

//  @Value("${spring.custom.reset_pass_template}")
//  private String resetPassTemplate;
//
//  @Value("${spring.custom.support_email}")
//  private String supportEmail;

  public JwtResponse loginUser(LoginDto userDto) {
    JwtResponse jwtResponse = new JwtResponse();
    try {
      String email = userDto.getEmail();
      User user = userService.findByEmail(email);

      if (user == null || !user.getServiceName().equals(userDto.getServiceName())) {
        jwtResponse.setErrors(Collections.singletonList("Cannot find user with this email!"));
        return jwtResponse;
      }

      Authentication authentication = new UsernamePasswordAuthenticationToken(
        email,
        userDto.getPassword()
      );
      authenticationManager.authenticate(authentication);

      String token = Jwts.builder()
        .setSubject(authentication.getName())
        .claim("authorities", authentication.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
        .compact();

      jwtResponse.setToken(token);
      jwtResponse.setMessage("OK");
      jwtResponse.setEmail(email);
      jwtResponse.setErrors(new ArrayList<>());
      return jwtResponse;
    } catch (Exception e) {
      jwtResponse.setErrors(Collections.singletonList("Cannot login!"));
      return jwtResponse;
    }
  }

  public void registerNewUserAccount(RegisterDto accountDto)  {
    User found = userService.findByUsernameOrEmail(
      accountDto.getUserName(), accountDto.getEmail()
    );
    if (found != null) {
      throw new UserAlreadyExistAuthenticationException("User already created!");
    }
    try {
      User user = new User();
      user.setServiceName(accountDto.getServiceName());
      user.setEmail(accountDto.getEmail());
      user.setUsername(accountDto.getUserName());
      user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
      userService.save(user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

//  private MailMessageDto constructResetTokenEmail(UUID token, User user) {
//    String url = resetPassTemplate + "/forgotPassword/" + token.toString();
//    return constructEmail("Here is your link to password reset: " + " \r\n" + url, user);
//  }
//
//  private MailMessageDto constructEmail(String body,
//                                        User user) {
//    MailMessageDto email = new MailMessageDto();
//    email.setSubject("Reset Password");
//    email.setText(body);
//    email.setTo(user.getEmail());
//    email.setFrom(supportEmail);
//    return email;
//  }

//  public void resetUserPassword(User user) {
//    UUID token = UUID.randomUUID();
//    PasswordResetToken passwordResetToken = new PasswordResetToken(
//      token, user
//    );
//    passwordResetTokenRepository.save(passwordResetToken);
//    constructResetTokenEmail(token, user);
//  }

  public void logoutUser() {
  }
}
