package org.example.medicalrecord.service;


import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.data.dto.UserSignupDto;
import org.example.medicalrecord.data.entity.User;

public interface AuthenticationService {

    User registerUser(UserSignupDto userSignupDto);

    UserDto getLoggedInUser();

}
