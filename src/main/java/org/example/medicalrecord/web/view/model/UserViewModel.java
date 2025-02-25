package org.example.medicalrecord.web.view.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class UserViewModel {

    private long id;

    @NotBlank
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    private Set<String> authorities;

}
