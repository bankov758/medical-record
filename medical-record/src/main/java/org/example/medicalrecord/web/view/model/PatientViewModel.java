package org.example.medicalrecord.web.view.model;

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
public class PatientViewModel extends UserViewModel {

    @NotBlank
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Invalid EGN format."
    )
    private String egn;

    //@DateTimeFormat(pattern = "mm-dd-yyyy")
    @NotNull
    @PastOrPresent(message = "Date cannot be in the future.")
    private LocalDate lastPaidMedicalInsurance;

    @NotNull(message = "Every patient should have a GP")
    private Long gpId;

}
