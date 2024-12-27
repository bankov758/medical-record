package org.example.medicalrecord.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Speciality extends BaseEntity {

    private String specialtyName;

    @JsonBackReference
    @ManyToMany(mappedBy = "specialities", fetch = FetchType.EAGER)
    private Set<Doctor> doctors;

}
