package org.example.medicalrecord.data.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Record extends BaseEntity {

    private Doctor doctor;

    private Patient patient;

    private Diagnose diagnose;

    private String receipt;

    private Date visitDate;

    private SickLeave sickLeave;

}
