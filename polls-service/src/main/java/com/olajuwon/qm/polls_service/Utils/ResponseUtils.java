package com.olajuwon.qm.polls_service.Utils;


import java.time.LocalDateTime;
import java.util.UUID;

public class ResponseUtils {
    public static <T> ApiResponse<T> createSuccessResponse(T data, String message) {
        return ApiResponse.<T>builder()
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .referenceId(UUID.randomUUID().toString())
                .status(true)
                .message(message)
                .data(data)
                .build();
    }


    public static <T> ApiResponse<T> createFailureResponse(String error, String message) {
        return ApiResponse.<T>builder()
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .referenceId(UUID.randomUUID().toString())
                .status(false)
                .message(message)
                .error(error)
                .build();
    }
}
