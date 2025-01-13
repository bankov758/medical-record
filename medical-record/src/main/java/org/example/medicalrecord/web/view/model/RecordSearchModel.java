package org.example.medicalrecord.web.view.model;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecordSearchModel {

    private String doctorFirstName;

    private String doctorLastName;

    private String patientFirstName;

    private String patientLastName;

    private String patientEgn;

    private String diagnoseName;

    private LocalDate visitDateFrom;

    private LocalDate visitDateTo;

}
