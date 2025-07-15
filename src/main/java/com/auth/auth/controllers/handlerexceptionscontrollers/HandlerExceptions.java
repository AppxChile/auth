package com.auth.auth.controllers.handlerexceptionscontrollers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth.auth.errors.ErrorResponse;
import com.auth.auth.exceptions.NotFounException;
import com.auth.auth.exceptions.SendMailExceptions;
import com.auth.auth.exceptions.SistemaException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HandlerExceptions {

    @ExceptionHandler(NotFounException.class)
    public ResponseEntity<Object> handlerNotFoundException(NotFounException e, HttpServletRequest request) {

        ErrorResponse error = maptoErrorResponse(e, request, HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(SendMailExceptions.class)
    public ResponseEntity<Object> handlerSesionActividadException(SendMailExceptions e,
            HttpServletRequest request) {

        ErrorResponse error = maptoErrorResponse(e, request, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(SistemaException.class)
    public ResponseEntity<Object> handlerSesionActividadException(SistemaException e,
            HttpServletRequest request) {

        ErrorResponse error = maptoErrorResponse(e, request, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    private <T extends Exception> ErrorResponse maptoErrorResponse(T e, HttpServletRequest request, HttpStatus status) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .mensaje(e.getMessage())
                .ruta(request.getRequestURI())
                .build();

    }

}
