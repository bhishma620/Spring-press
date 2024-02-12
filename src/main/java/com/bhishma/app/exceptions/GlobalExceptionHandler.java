package com.bhishma.app.exceptions;


import com.bhishma.app.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)

    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message=ex.getMessage();
        ApiResponse apiResponse=new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity< Map<String,String> > methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){

     Map<String,String> response=new HashMap<>();

     ex.getBindingResult().getAllErrors().forEach((error)->{
         String fieldName=((FieldError)error).getField();
         String defaultMeassage= error.getDefaultMessage();
        response.put(fieldName,defaultMeassage);
     });

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
