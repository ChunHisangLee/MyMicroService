package com.jack.accounts.exception;

import com.jack.accounts.dto.ErrorResponseDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles validation errors for request body.
   *
   * @param ex The exception thrown.
   * @param headers The headers.
   * @param status The status code.
   * @param request The web request.
   * @return A ResponseEntity containing a map of validation errors.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

    Map<String, String> validationErrors = new HashMap<>();
    List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

    for (ObjectError error : validationErrorList) {
      String fieldName = ((FieldError) error).getField();
      String validationMsg = error.getDefaultMessage();
      validationErrors.put(fieldName, validationMsg);
    }

    return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all other unhandled exceptions.
   *
   * @param exception The exception thrown.
   * @param request The web request.
   * @return A ResponseEntity containing the error details.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGlobalException(
      Exception exception, WebRequest request) {
    return createErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, exception);
  }

  /**
   * Handles ResourceNotFoundException.
   *
   * @param exception The exception thrown.
   * @param request The web request.
   * @return A ResponseEntity containing the error details.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
      ResourceNotFoundException exception, WebRequest request) {
    return createErrorResponse(request, HttpStatus.NOT_FOUND, exception);
  }

  /**
   * Handles CustomerAlreadyExistsException.
   *
   * @param exception The exception thrown.
   * @param request The web request.
   * @return A ResponseEntity containing the error details.
   */
  @ExceptionHandler(CustomerAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(
      CustomerAlreadyExistsException exception, WebRequest request) {
    return createErrorResponse(request, HttpStatus.CONFLICT, exception);
  }

  /**
   * Helper method to create a standard error response.
   *
   * @param request The web request.
   * @param status The HTTP status code.
   * @param exception The exception.
   * @return A ResponseEntity containing the error details.
   */
  private ResponseEntity<ErrorResponseDto> createErrorResponse(
      WebRequest request, HttpStatus status, Exception exception) {
    ErrorResponseDto errorResponse =
        new ErrorResponseDto(
            request.getDescription(false), status, exception.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, status);
  }
}
