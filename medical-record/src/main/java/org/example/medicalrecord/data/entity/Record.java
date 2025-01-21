package org.example.medicalrecord.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Record extends BaseEntity {

    @ManyToOne(optional = false)
    private Doctor doctor;

    @ManyToOne(optional = false)
    private Patient patient;

    @OneToOne(mappedBy = "record", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Diagnose diagnose;

    @Column(nullable = false)
    private Date visitDate;

    @OneToOne(mappedBy = "record", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private SickLeave sickLeave;

}
