package sa_team8.scoreboard.global.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
    log.warn("Login failed: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.of(ErrorCode.LOGIN_FAILED));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("Validation ERROR : {}", e.getBindingResult());
    return ResponseEntity.status(e.getStatusCode())
        .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, "유효성 검사가 실패했습니다."));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, "요청 형식이 올바르지 않습니다."));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
    log.warn("HTTP method not supported: {}", e.getMethod());
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.of(errorCode, "지원하지 않는 HTTP 메소드입니다: " + e.getMethod()));
  }

  /**
   * CustomException
   */
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(ApplicationException e) {
    ErrorCode errorCode = e.getErrorCode();

    log.warn("[CustomException] {}", errorCode.getCode());
    e.printStackTrace();

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.of(errorCode, e.getMessage()));
  }

}
