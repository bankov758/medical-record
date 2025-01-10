package org.example.medicalrecord.data.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientDto extends UserDto {

    @NotBlank
    @Size(min = 3, max = 20, message="Last name must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "EGN can not be empty")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Invalid EGN format."
    )
    private String egn;

    @NotNull(message = "Last paid medical insurance date can not be null")
    @PastOrPresent(message = "Last paid medical insurance date  cannot be in the future.")
    private LocalDate lastPaidMedicalInsurance;

    @NotNull(message = "Every patient should have a GP")
    private long gpId;

}
