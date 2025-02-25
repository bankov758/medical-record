package org.example.medicalrecord.web.view.model;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnoseViewModel {

    private String diagnoseName;

    private Integer occurrenceCount;

}
