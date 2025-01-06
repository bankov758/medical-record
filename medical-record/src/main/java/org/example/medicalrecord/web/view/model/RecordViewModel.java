package org.example.medicalrecord.web.view.model;

import lombok.*;

import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecordViewModel {

    private String doctor;

    private String patient;

    private String diagnose;

    private String receipt;

    private Date visitDate;

    private int sickLeave;

}
