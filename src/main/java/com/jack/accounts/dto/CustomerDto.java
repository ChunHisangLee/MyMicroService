package com.jack.accounts.dto;

import com.jack.accounts.constant.AccountsConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "Customer", description = "Schema to hold Customer and Account information")
@Data
public class CustomerDto {
  @Schema(description = "Name of the customer", example = "Jack Lee")
  @NotEmpty(message = "Name cannot be empty")
  @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
  private String name;

  @Schema(description = "Email of the customer", example = "9M6dM@example.com")
  @NotEmpty(message = "Email cannot be empty")
  @Email(message = "Invalid email format")
  private String email;

  @Schema(description = "Mobile number of the customer", example = "9876543210")
  @Pattern(regexp = "^$|[0-9]{10}", message = AccountsConstants.MOBILE_NUMBER_ERROR)
  private String mobileNumber;

  @Schema(description = "Account details of the customer")
  private AccountsDto accountsDto;
}
