package com.jack.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "customer_id")
  private Long customerId;

  private String name;
  private String email;

  @Column(name = "mobile_number")
  private String mobileNumber;
}
