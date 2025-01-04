package org.example.medicalrecord.service;


import org.example.medicalrecord.web.view.model.SignupViewModel;
import org.example.medicalrecord.data.entity.User;

public interface AuthenticationService {

    User registerUser(SignupViewModel signupViewModel);

}
