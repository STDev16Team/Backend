package com.STDev.bbang.global.response;

import com.STDev.bbang.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private String message;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private HttpStatus httpStatus;

    public static <T> ApiResponse<T> createSuccess(T data, String message) {
        return new ApiResponse<>(SUCCESS_STATUS, data, message, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> createSuccessWithNoContent(String message) {
        return new ApiResponse<>(SUCCESS_STATUS, null, message, HttpStatus.OK);
    }

    // 예외 발생으로 API 호출 실패시 반환
    public static <T> ApiResponse<T> createError(ErrorCode errorCode) {
        return new ApiResponse<>(ERROR_STATUS, null,  errorCode.getMessage(), errorCode.getHttpStatus());
    }

    private ApiResponse(String status, T data, String message, HttpStatus httpStatus) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}