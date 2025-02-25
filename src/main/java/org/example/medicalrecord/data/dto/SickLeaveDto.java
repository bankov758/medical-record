package org.example.medicalrecord.data.dto;

import lombok.*;
import org.example.medicalrecord.data.entity.Record;

import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SickLeaveDto {

    private Date startDate;

    private int leaveDays;

    private Record record;

}
