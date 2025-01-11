package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByPatientId(long patientId);
}
