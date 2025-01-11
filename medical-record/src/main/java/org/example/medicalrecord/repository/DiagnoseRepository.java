package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    Diagnose findByRecordId(Long recordId);
}
