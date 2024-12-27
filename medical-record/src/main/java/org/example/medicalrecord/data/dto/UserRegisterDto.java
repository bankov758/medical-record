package org.example.medicalrecord.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String confirmPassword;

}
