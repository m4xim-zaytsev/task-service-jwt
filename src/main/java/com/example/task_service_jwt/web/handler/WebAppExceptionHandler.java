package com.example.task_service_jwt.web.handler;

import com.example.task_service_jwt.exception.AlreadyExistException;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.exception.IllegalAccessException;
import com.example.task_service_jwt.exception.RefreshTokenException;
import com.example.task_service_jwt.web.model.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value =  RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex, WebRequest webRequest){
        return buildResponse(HttpStatus.FORBIDDEN,ex,webRequest);
    }

    @ExceptionHandler(value =  IllegalAccessException.class)
    public ResponseEntity<ErrorResponseBody> illegalAccessExceptionHandler(IllegalAccessException ex, WebRequest webRequest){
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
                        .message(ex.getMessage())
                        .description(webRequest.getDescription(false))
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> genericExceptionHandler(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = errorMessages.stream().collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Validation error: " + errorMessage));
    }
}
