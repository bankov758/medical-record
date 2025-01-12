package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepository extends JpaSpecificationExecutor<Record>, JpaRepository<Record, Long> {

    List<Record> findAllByPatientId(long patientId);

    @Query("SELECT r FROM Record r " +
            "WHERE (:doctorFirstName IS NULL OR r.doctor.firstName = :doctorFirstName) " +
            "AND (:doctorLastName IS NULL OR r.doctor.lastName = :doctorLastName) " +
            "AND (:patientFirstName IS NULL OR r.patient.firstName = :patientFirstName) " +
            "AND (:patientLastName IS NULL OR r.patient.lastName = :patientLastName) " +
            "AND (:patientEgn IS NULL OR r.patient.egn = :patientEgn) " +
            "AND (:diagnoseName IS NULL OR r.diagnose.diagnoseName = :diagnoseName) " +
            "AND (:visitDate IS NULL OR r.visitDate = :visitDate)")
    List<Record> filterRecords(
            @Param("doctorFirstName") String doctorFirstName,
            @Param("doctorLastName") String doctorLastName,
            @Param("patientFirstName") String patientFirstName,
            @Param("patientLastName") String patientLastName,
            @Param("patientEgn") String patientEgn,
            @Param("diagnoseName") String diagnoseName,
            @Param("visitDate") Date visitDate
    );

}
