package org.example.medicalrecord.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@Inheritance(strategy = InheritanceType.JOINED)
public class Doctor extends User {

    @Column(name = "is_gp")
    private Boolean isGp;

    @JsonManagedReference
    @ManyToMany
    private Set<Speciality> specialities;

    @OneToMany(mappedBy = "gp")
    @JsonIgnore
    private Set<Patient> patients;

}
