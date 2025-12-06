package sa_team8.scoreboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // 400 Bad Request
  INVALID_REQUEST(400, "C400", "잘못된 요청입니다."),
  INVALID_INPUT_VALUE(400, "C401", "입력값이 유효하지 않습니다."),
  INVALID_REFRESHTOKEN(400, "C402", "유효하지 않은 Refresh Token 입니다."),

  // 401 Unauthorized
  UNAUTHORIZED(401, "A401", "인증이 필요합니다."),
  LOGIN_FAILED(401, "A402", "아이디 또는 비밀번호가 일치하지 않습니다."),

  // 403 Forbidden
  FORBIDDEN(403, "A403", "접근 권한이 없습니다."),

  // 409 Conflict
  ALREADY_EXISTS(409, "C409", "이미 존재하는 데이터입니다."),
  USER_DUPLICATED(409, "C410", "이미 가입된 유저입니다."),

  // 500 Internal Server Error
  INTERNAL_SERVER_ERROR(500, "C500", "서버 내부 오류가 발생했습니다."),

  // 1000 Competition Error
  COMPETITION_NOT_FOUND(404, "1000", "대회를 찾을 수 없습니다."),
  COMPETITION_NOT_MANAGED(403, "1001", "해당 대회에 대한 관리 권한이 없습니다."),
  INVALID_STATE_CHANGE(409,"1002", "대회 상태를 변경할 수 없는 요청입니다."),

  // 2000 Team Error
  TEAM_NOT_FOUND(404, "2000", "Team을 찾을 수 없습니다."),

  // 3000 Manager Error
  MANAGER_NOT_FOUND(404, "3000", "Manager 를 찾을 수 없습니다."),

  // 4000 Score Error
  SCORE_BOARD_NOT_FOUND(404, "4000","ScoreBoard 를 찾을 수 없습니다." );

  private final int status;
  private final String code;
  private final String message;
}
