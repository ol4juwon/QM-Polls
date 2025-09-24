package com.olajuwon.qm.vote_publisher.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private LocalDateTime requestTime;
    private String requestType;
    private String referenceId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

//    public static <T> ApiResponseBuilder<T> newBuilder() {
//        return ApiResponse.<T>builder();
//    }

    @Builder
    public ApiResponse(LocalDateTime requestTime, String requestType, String referenceId,boolean status, String message, T data ){
        this.status = status;
        this.message = message;
        this.requestType = requestType;
        this.referenceId = referenceId;
        this.requestTime = requestTime;
        this.data = data;
    }
    @Builder
    public ApiResponse(LocalDateTime requestTime, String requestType, String referenceId,boolean status, String message, String error ){
        this.status = status;
        this.message = message;
        this.requestType = requestType;
        this.referenceId = referenceId;
        this.requestTime = requestTime;
        this.error = error;
    }
}
