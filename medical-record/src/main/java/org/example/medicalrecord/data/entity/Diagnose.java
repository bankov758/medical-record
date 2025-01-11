package org.example.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diagnose extends BaseEntity {

    @Column(nullable = false)
    private String diagnoseName;

    @Column(nullable = false)
    private String receipt;

    @OneToOne(optional = false)
    private Record record;

}
