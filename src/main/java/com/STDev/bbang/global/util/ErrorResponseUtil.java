package com.STDev.bbang.global.util;

import com.STDev.bbang.global.exception.ErrorCode;
import com.STDev.bbang.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ErrorResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendErrorResponse(
            HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        String jsonResponse = objectMapper.writeValueAsString(ApiResponse.createError(errorCode));
        response.getWriter().write(jsonResponse);
    }
}
