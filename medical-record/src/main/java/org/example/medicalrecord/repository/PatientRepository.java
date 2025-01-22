package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PatientRepository extends JpaSpecificationExecutor<Patient>, JpaRepository<Patient, Long> {
    Optional<Patient> findByEgnAndFirstNameAndLastName(String egn, String firstName, String lastName);
}
