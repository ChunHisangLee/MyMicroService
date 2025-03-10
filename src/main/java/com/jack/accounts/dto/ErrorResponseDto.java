package com.jack.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Schema(name = "ErrorResponse", description = "Schema to hold error response information")
@Data
@AllArgsConstructor
public class ErrorResponseDto {
  @Schema(description = "API path invoked by client", example = "/create")
  private String apiPath;

  @Schema(description = "Error code representing the error happened", example = "500")
  private HttpStatus errorCode;

  @Schema(
      description = "Error message representing the error happened",
      example = "Internal Server Error")
  private String errorMessage;

  @Schema(
      description = "Time representing when the error happened",
      example = "2023-06-30T10:00:00")
  private LocalDateTime errorTime;
}
