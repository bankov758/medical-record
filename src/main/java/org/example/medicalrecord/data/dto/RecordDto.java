package org.example.medicalrecord.data.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RecordDto {

    private long id;

    @NotNull(message = "Medical record should have a doctor")
    private Long doctorId;

    private String doctorFirstName;

    private String doctorLastName;

    @NotBlank
    @Size(min = 3, max = 20, message = "Patient first name must be between 3 and 20 characters")
    private String patientFirstName;

    @NotBlank
    @Size(min = 3, max = 20, message = "Patient last name must be between 3 and 20 characters")
    private String patientLastName;

    @NotBlank(message = "Patient EGN can not be empty")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Invalid EGN format."
    )
    private String patientEgn;

    @NotBlank(message = "Diagnose should have a name")
    @Size(min = 3, message="Diagnose name must be between 3 and 20 characters")
    private String diagnoseName;

    @NotBlank(message = "Diagnose needs receipt")
    @Size(min = 10, message="Receipt must be at least 10 characters")
    private String receipt;

    @NotNull(message = "Visit date can not be null")
    @FutureOrPresent(message = "Visit date cannot be in the past.")
    private Date visitDate;

    private Date startDate;

    private int leaveDays;

}
