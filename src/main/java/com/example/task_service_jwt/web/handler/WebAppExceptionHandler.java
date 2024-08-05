package com.example.task_service_jwt.web.handler;

import com.example.task_service_jwt.exception.AlreadyExistException;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value =  RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex, WebRequest webRequest){
        return buildResponse(HttpStatus.FORBIDDEN,ex,webRequest);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistHandler(AlreadyExistException exception, WebRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST,exception,request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> notFoundHandler(EntityNotFoundException exception, WebRequest request){
        return buildResponse(HttpStatus.NOT_FOUND,exception,request);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseBody.builder()
                        .message(ex.getMessage()).description(webRequest.getDescription(false)).build());
    }


}
