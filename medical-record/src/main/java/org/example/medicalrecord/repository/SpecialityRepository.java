package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}
