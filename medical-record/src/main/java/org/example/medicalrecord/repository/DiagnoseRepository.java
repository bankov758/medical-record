package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {

    Diagnose findByRecordId(Long recordId);

    long countByDiagnoseName(String diagnoseName);

    @Query("SELECT d.diagnoseName, COUNT(d) as cnt FROM Diagnose d " +
            "GROUP BY d.diagnoseName " +
            "ORDER BY cnt DESC")
    List<Object[]> findTop3DiagnosesByNameCount();

    @Query("SELECT new org.example.medicalrecord.data.dto.DiagnoseDto(COUNT(d), d.diagnoseName) FROM Diagnose d " +
            "GROUP BY d.diagnoseName " +
            "ORDER BY COUNT(d) DESC")
    List<DiagnoseDto> findTop3DiagnosesByNameCount1();

}
