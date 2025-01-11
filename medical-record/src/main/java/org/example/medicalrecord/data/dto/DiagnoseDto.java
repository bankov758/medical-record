package org.example.medicalrecord.data.dto;

import lombok.*;
import org.example.medicalrecord.data.entity.Record;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnoseDto {

    private String diagnoseName;

    private String receipt;

    private Record record;

}
