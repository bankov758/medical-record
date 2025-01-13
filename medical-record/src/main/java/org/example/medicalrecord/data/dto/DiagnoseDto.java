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

    private Long occurrenceCount;

    public DiagnoseDto(String diagnoseName, String receipt, Record record) {
        this.diagnoseName = diagnoseName;
        this.receipt = receipt;
        this.record = record;
    }

    public DiagnoseDto(Long occurrenceCount, String diagnoseName) {
        this.occurrenceCount = occurrenceCount;
        this.diagnoseName = diagnoseName;
    }
}
