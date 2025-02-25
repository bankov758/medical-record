package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.entity.SickLeave;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.SickLeaveRepository;
import org.example.medicalrecord.service.SickLeaveService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
@AllArgsConstructor
public class SickLeaveServiceImpl implements SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;

    @Override
    public String getMonthWithMostSickLeaves() {
        List<Object[]> results = sickLeaveRepository.findMonthWithMostSickLeaves();
        if (results.isEmpty()) {
            return "No sick leaves found.";
        }
        Object[] topResult = results.getFirst();
        int month = (int) topResult[0];
        long count = (long) topResult[1];
        String monthName = Month.of(month).name();
        monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();
        return String.format("The month with the most sick leaves is %s with %d sick leaves.", monthName, count);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public SickLeave updateSickLeave(SickLeaveDto SickLeaveDto, long id) {
        SickLeave sickLeave = sickLeaveRepository.findByRecordId(id).orElseThrow(() -> new EntityNotFoundException(SickLeave.class, "record with", id));
        if (sickLeave == null) {
            sickLeave = new SickLeave();
        }
        sickLeave.setLeaveDays(SickLeaveDto.getLeaveDays());
        sickLeave.setStartDate(SickLeaveDto.getStartDate());
        sickLeave.setRecord(SickLeaveDto.getRecord());
        return sickLeaveRepository.save(sickLeave);
    }

}
