package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

    Optional<Speciality> findBySpecialtyName(String specialtyName);

    List<Speciality> findAllByDoctors_Id(Long doctorId);

}
