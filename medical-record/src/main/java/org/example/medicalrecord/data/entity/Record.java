package org.example.medicalrecord.data.entity;

import jakarta.persistence.*;
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

    @OneToOne(mappedBy = "record", cascade = CascadeType.PERSIST)
    private Diagnose diagnose;

    @Column(nullable = false)
    private Date visitDate;

    @OneToOne(mappedBy = "record", cascade = CascadeType.PERSIST)
    private SickLeave sickLeave;

}
