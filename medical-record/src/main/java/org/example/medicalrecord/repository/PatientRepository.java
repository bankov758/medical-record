package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
