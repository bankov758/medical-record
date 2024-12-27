package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
