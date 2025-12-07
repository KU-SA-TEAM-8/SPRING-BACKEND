package sa_team8.scoreboard.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

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
