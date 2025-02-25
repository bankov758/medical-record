package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaSpecificationExecutor<Record>, JpaRepository<Record, Long> {

    List<Record> findAllByPatientId(long patientId);

}
