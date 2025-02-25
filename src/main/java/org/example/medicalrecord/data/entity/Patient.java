package org.example.medicalrecord.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public class Patient extends User {

    @Column(nullable = false)
    private String egn;

    private Date lastPaidMedicalInsurance;

    @ManyToOne
    private Doctor gp;

}
