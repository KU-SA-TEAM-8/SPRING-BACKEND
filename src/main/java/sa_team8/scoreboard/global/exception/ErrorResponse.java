package sa_team8.scoreboard.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private final int status;
  private final String code;
  private final String message;
  private final String detail;

  public static ErrorResponse of(ErrorCode errorCode, String detail) {
    return ErrorResponse.builder()
        .status(errorCode.getStatus())
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .detail(detail)
        .build();
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return of(errorCode, null);
  }
}
