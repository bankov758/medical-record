package org.example.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class SickLeave extends BaseEntity {

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private int leaveDays;

    @OneToOne(optional = false)
    private Record record;

}
