package org.example.medicalrecord.service;


import org.example.medicalrecord.data.dto.UserLoginDto;
import org.example.medicalrecord.data.dto.UserLoginResponseDto;
import org.example.medicalrecord.data.dto.UserRegisterDto;
import org.example.medicalrecord.data.entity.User;

public interface AuthenticationService {
    User registerUser(UserRegisterDto userRegisterDto);

    UserLoginResponseDto loginUser(UserLoginDto userLoginDto);
}
