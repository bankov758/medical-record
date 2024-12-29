package org.example.medicalrecord.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @ManyToOne(optional = false)
    private Doctor doctor;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Diagnose diagnose;

    private String receipt;

    private Date visitDate;

    @OneToOne
    private SickLeave sickLeave;

}
