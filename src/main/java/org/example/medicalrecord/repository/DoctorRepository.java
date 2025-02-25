package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findAllByIsGpTrue();

    @Query("SELECT d " +
            "FROM Doctor d " +
            "JOIN d.records r " +
            "JOIN r.sickLeave s " +
            "GROUP BY d " +
            "ORDER BY COUNT(s) DESC limit 3")
    List<Doctor> findTop3ByOrderByRecordsSickLeaveDesc();

    boolean existsByIdAndSpecialitiesSpecialtyName(Long doctorId, String specialtyName);

    boolean existsByIdAndSpecialitiesId(Long doctorId, Long specialtyId);

    int countByRecordsDoctorId(long doctorId);

    int countByIdAndRecordsSickLeaveIsNotNull(long doctorId);

    int countByPatientsGpId(long doctorId);

}
