package com.jack.accounts.dto;

import com.jack.accounts.constant.AccountsConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {
  @NotEmpty(message = "Name cannot be empty")
  @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
  private String name;

  @NotEmpty(message = "Email cannot be empty")
  @Email(message = "Invalid email format")
  private String email;

  @Pattern(regexp = "^$|[0-9]{10}", message = AccountsConstants.MOBILE_NUMBER_ERROR)
  private String mobileNumber;

  private AccountsDto accountsDto;
}
