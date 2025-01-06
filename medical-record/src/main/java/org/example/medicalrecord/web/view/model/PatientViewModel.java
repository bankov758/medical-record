package org.example.medicalrecord.web.view.model;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientViewModel extends UserViewModel {

    private String egn;

    //@DateTimeFormat(pattern = "mm-dd-yyyy")
    private LocalDate lastPaidMedicalInsurance;

    private Long gpId;

}
