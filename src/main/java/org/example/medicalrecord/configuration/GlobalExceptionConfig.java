package org.example.medicalrecord.configuration;

import org.example.medicalrecord.exceptions.AuthorizationFailureException;
import org.example.medicalrecord.exceptions.DuplicateEntityException;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(Exception.class)
    protected String handleException(Exception exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "/error";
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public String handleException(EntityNotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);
        return "/error";
    }

    @ExceptionHandler({DuplicateEntityException.class})
    public String handleException(DuplicateEntityException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("status", HttpStatus.CONFLICT);
        return "/error";
    }

    @ExceptionHandler({AuthorizationFailureException.class})
    public String handleException(AuthorizationFailureException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("status", HttpStatus.UNAUTHORIZED);
        return "/error";
    }

}
