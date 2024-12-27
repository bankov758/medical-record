package org.example.medicalrecord.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @NotNull(message = "Username can't be empty")
    @Size(min = 2, max = 40, message = "Username should be between 2 and 40 symbols")
    private String username;

    @NotNull(message = "Password can't be empty")
    @Size(min = 8, message = "Password should be longer than 8 symbols")
    private String password;

}
