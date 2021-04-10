package com.nuxxxcake.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "password_reset")
@NoArgsConstructor
public class PasswordResetToken {
  @Id
  @GeneratedValue( generator = "uuid2" )
  @Type(type = "uuid-char")
  private UUID id;

  @Type(type = "uuid-char")
  @Column(nullable = false)
  private UUID token;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "userId")
  private User user;

  @Column(nullable = false)
  private Date expireDate;

  public PasswordResetToken(UUID token, User user) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, 1);

    this.token = token;
    this.user = user;
    this.expireDate = cal.getTime();
  }
}

