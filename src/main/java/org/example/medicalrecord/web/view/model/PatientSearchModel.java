package org.example.medicalrecord.web.view.model;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientSearchModel {

    private String firstName;

    private String lastName;

    private String egn;

    private LocalDate lastPaidMedicalFrom;

    private LocalDate lastPaidMedicalTo;

    private Long gpId;

}
