package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.dto.UserLoginDto;
import org.example.medicalrecord.data.dto.UserOutDto;
import org.example.medicalrecord.data.dto.UserRegisterDto;
import org.example.medicalrecord.exceptions.AuthenticationFailureException;
import org.example.medicalrecord.exceptions.DuplicateEntityException;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.service.AuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.example.medicalrecord.util.DataUtil.getDefaultMessages;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final ModelMapper userMapper;

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult errors) {
        try {
            if (errors.hasErrors()) {
                //return ResponseEntity.badRequest().body(getDefaultMessages(errors));
                return "login";
            }
            //return ResponseEntity.ok().body(authenticationService.loginUser(userLoginDto));
            authenticationService.loginUser(userLoginDto);
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            errors.rejectValue("username", "auth.error", e.getMessage());
            //return ResponseEntity.badRequest().body(getDefaultMessages(errors));
            return "login";
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto register, BindingResult errors) {
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(getDefaultMessages(errors));
            }
            if (!register.getPassword().equals(register.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "password_error",
                        "Password confirmation should match password.");
                return ResponseEntity.badRequest().body(getDefaultMessages(errors));
            }
            UserOutDto newUser = userMapper.map(authenticationService.registerUser(register), UserOutDto.class);
            return ResponseEntity.ok().body(newUser);
        } catch (DuplicateEntityException | EntityNotFoundException ex) {
            String[] exceptionMessage = ex.getMessage().split(" ");
            String fieldName = exceptionMessage[2];
            errors.rejectValue(fieldName, "user_error", ex.getMessage());
            return ResponseEntity.badRequest().body(getDefaultMessages(errors));
        }
    }

}

