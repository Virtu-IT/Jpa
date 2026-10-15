package be.abis.exercise.controller;

import be.abis.exercise.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({PersonNotFoundException.class, CourseNotFoundException.class})
    public ResponseEntity<Map<String, String>> notFound(Exception e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({PersonAlreadyExistsException.class, PersonCanNotBeDeletedException.class,
            SessionCanNotBeDeletedException.class})
    public ResponseEntity<Map<String, String>> conflict(Exception e) {
        return error(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(EnrolException.class)
    public ResponseEntity<Map<String, String>> badRequest(Exception e) {
        return error(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, Exception e) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(e.getMessage())));
    }
}
