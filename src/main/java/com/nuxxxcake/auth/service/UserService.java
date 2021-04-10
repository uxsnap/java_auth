package com.nuxxxcake.auth.service;

import com.nuxxxcake.auth.dto.UserInfoDto;
import com.nuxxxcake.auth.entity.User;
import com.nuxxxcake.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public User loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(s);
    if (user == null) {
      throw new UsernameNotFoundException("Not found!");
    }
    return user;
  }


  public User findByEmail(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("Not found!");
    }
    return user;
  }

  public User findByUsernameOrEmail(String name, String email) {
    return userRepository.findByUsernameOrEmail(name, email);
  }

  public void save(User user) {
    userRepository.save(user);
  }
}

