package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.web.view.model.SignupViewModel;
import org.example.medicalrecord.exceptions.DuplicateEntityException;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/signup")
    public String showRegisterPage(Model model) {
        model.addAttribute("signupViewModel", new SignupViewModel());
        return "sign-up";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("signupViewModel") SignupViewModel signupViewModel, BindingResult errors) {
        try {
            if (errors.hasErrors()) {
                return "sign-up";
            }
            if (!signupViewModel.getPassword().equals(signupViewModel.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "password_error",
                        "Password confirmation should match password.");
                return "sign-up";
            }
            authenticationService.registerUser(signupViewModel);
            return "redirect:/login";
        } catch (DuplicateEntityException | EntityNotFoundException ex) {
            String[] exceptionMessage = ex.getMessage().split(" ");
            String fieldName = exceptionMessage[2];
            errors.rejectValue(fieldName, "user_error", ex.getMessage());
            return "sign-up";
        }
    }

}

