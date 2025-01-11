package org.example.medicalrecord.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientDto extends UserDto {

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
