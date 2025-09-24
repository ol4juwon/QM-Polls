package com.olajuwon.qm.polls_service.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.postgresql.util.PSQLException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.olajuwon.qm.polls_service.Utils.ResponseUtils.createFailureResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getRootCause();

        if (cause instanceof PSQLException) {


            PSQLException sqlException = (PSQLException) cause;
            String sqlErrorCode = sqlException.getSQLState();
            String errorMessage = sqlException.getMessage();

            if (sqlErrorCode.equalsIgnoreCase("23505")) {
                // 23505 is the SQL error code for unique constraint violation
                // Extract more details based on your database's error codes
                String constraintName = extractConstraintName(errorMessage);
                return new ResponseEntity<>(createFailureResponse(constraintName, "Duplicate Error"), HttpStatus.CONFLICT);
            }
        }else if(cause instanceof DataIntegrityViolationException){
            PSQLException sqlException = (PSQLException) cause;
            String sqlErrorCode = sqlException.getSQLState();
            String errorMessage = sqlException.getMessage();

            if (sqlErrorCode.equalsIgnoreCase("23505")) {
                // 23505 is the SQL error code for unique constraint violation
                // Extract more details based on your database's error codes
                String constraintName = extractConstraintName(errorMessage);
                return new ResponseEntity<>(createFailureResponse(constraintName, "Duplicate Error"), HttpStatus.CONFLICT);
            }
        }
        System.out.println("Cause class: {}"+ ex.getRootCause());
        String constraintName = extractConstraintName(ex.getMessage());
        // Handle other types of DataIntegrityViolationException or provide a generic error message
        return new ResponseEntity<>(createFailureResponse("","Duplicate error "+constraintName), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
//        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .requestTime(LocalDateTime.now())
                .status(false)
                .error("Validation Failed")
                .message(errors.toString())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    private String extractConstraintName(String errorMessage) {
        Pattern pattern = Pattern.compile("Key \\((\\w+)\\)=\\((.*)\\) already exists");
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            String response  ="Key: "+matcher.group(1)+", value:"+matcher.group(2);
            return response;
        } else {
            return "UnknownConstraint";
        }
    }
}
