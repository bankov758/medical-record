package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.entity.SickLeave;
import org.example.medicalrecord.repository.SickLeaveRepository;
import org.example.medicalrecord.service.SickLeaveService;
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
    public SickLeave updateSickLeave(SickLeaveDto SickLeaveDto, long id) {
        SickLeave SickLeave = sickLeaveRepository.findByRecordId(id);
        if (SickLeave == null) {
            SickLeave = new SickLeave();
        }
        SickLeave.setLeaveDays(SickLeaveDto.getLeaveDays());
        SickLeave.setStartDate(SickLeaveDto.getStartDate());
        SickLeave.setRecord(SickLeaveDto.getRecord());
        return sickLeaveRepository.save(SickLeave);
    }

}
