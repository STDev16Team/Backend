package com.STDev.bbang.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    NOT_FOUND_API_URL(HttpStatus.NOT_FOUND, "요청한 API url을 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

    // Login
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    BAD_REQUEST_LOGIN(HttpStatus.BAD_REQUEST, "값을 정확히 입력해주세요."),
    FAIL_DELETE_MEMBER(HttpStatus.BAD_REQUEST, "회원 탈퇴 실패했습니다."),

    // Game
    DIE_GAME(HttpStatus.BAD_REQUEST, "이미 죽은 게임입니다."),
    BAD_REQUEST_GAME(HttpStatus.BAD_REQUEST, "해당 단계가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
