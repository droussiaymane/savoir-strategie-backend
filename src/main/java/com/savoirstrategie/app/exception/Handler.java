package com.savoirstrategie.app.exception;


import com.savoirstrategie.app.helpers.CODE;
import com.savoirstrategie.app.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = {"com.savoirstrategie.app"})
@Slf4j
public class Handler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> handleValidationErrors(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
        log.error("Error during request processing.", exception);
        Response<Object> response;
        if(errors.size()>0){

       response = Response.builder()
                .data(getErrorsMap(errors))
                .message(errors.get(0))
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
    }
    else{

        response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
    }


        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler(value=UnverifiedException.class)
    public ResponseEntity<Response<?>> unverifiedException(Exception exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value=EmailSenderException.class)
    public ResponseEntity<Response<?>> emailSenderException(Exception exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.INTERNAL_SERVER_ERROR.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value=ExpiredTokenException.class)
    public ResponseEntity<Response<?>> expiredTokenException(Exception exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value=TokenNotFoundException.class)
    public ResponseEntity<Response<?>> tokenNotFoundException(Exception exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value=EmailAlreadyExistsException.class)
    public ResponseEntity<Response<?>> emailAlreadyExistsException(Exception exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value=UserAlreadyVerifiedException.class)
    public ResponseEntity<Response<?>> UserAlreadyVerifiedException(Exception exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value=ValidationException.class)
    public ResponseEntity<Response<?>> exception(ValidationException exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.BAD_REQUEST.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Response<?>> exception(SQLIntegrityConstraintViolationException exception) {
        log.error("Error during request processing.", exception);
        Response<Object> response = Response.builder()
                .message(exception.getMessage())
                .code(CODE.INTERNAL_SERVER_ERROR.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



        @ExceptionHandler(value = {CannotCreateTransactionException.class})
        public ResponseEntity<?> cannotCreateTransactionException(CannotCreateTransactionException exception) {
            if (exception.contains(ConnectException.class)) {
                log.error("DB ConnectException :  {}", exception.getMessage());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }


    // fallback method
    @ExceptionHandler(Exception.class) // exception handled
    public ResponseEntity<Response<?>> handleExceptions(
            Exception e
    ) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        // converting the stack trace to String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        Response<Object> response = Response.builder()
                .message(e.getMessage())
                .code(CODE.INTERNAL_SERVER_ERROR.getId())
                .success(false)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);


    }
}
