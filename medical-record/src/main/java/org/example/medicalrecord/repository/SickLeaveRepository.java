package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {

    SickLeave findByRecordId(Long recordId);

    @Query("SELECT FUNCTION('MONTH', s.startDate) AS month, COUNT(s) AS count " +
            "FROM SickLeave s " +
            "GROUP BY FUNCTION('MONTH', s.startDate) " +
            "ORDER BY count DESC")
    List<Object[]> findMonthWithMostSickLeaves();

}
