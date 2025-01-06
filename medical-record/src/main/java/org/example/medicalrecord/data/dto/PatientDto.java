package org.example.medicalrecord.data.dto;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientDto {

    private long id;

    private String firstName;

    private String lastName;

    private String username;

    private String egn;

    private LocalDate lastPaidMedicalInsurance;

    private long gpId;

}
