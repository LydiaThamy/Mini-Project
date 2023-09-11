package mini_project.server.exception;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<String> handleAccessTokenException(AccessTokenException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
            new ErrorMessage(400, new Date(), ex.getMessage(), request.getRequestURI()).toString()
        );
    }
    
    @ExceptionHandler(UserAccessException.class)
    public ResponseEntity<String> handleUserAccessException(UserAccessException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
            new ErrorMessage(400, new Date(), ex.getMessage(), request.getRequestURI()).toString()
        );
    }
}
