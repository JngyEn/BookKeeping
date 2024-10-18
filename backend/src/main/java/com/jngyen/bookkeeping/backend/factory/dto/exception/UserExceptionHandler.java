package com.jngyen.bookkeeping.backend.factory.dto.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserExceptionHandler {

    public ResponseEntity<Map<String,String>> handleUserException(MethodArgumentNotValidException e) {
        Map<String, String> map = new HashMap<>();
        
        e.getBindingResult().getFieldErrors().forEach(error -> 
            map.put(error.getField(), error.getDefaultMessage())
            );
        
        return ResponseEntity.badRequest().body(map);
    }
    
}
