package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.entity.SickLeave;
import org.example.medicalrecord.repository.SickLeaveRepository;
import org.example.medicalrecord.service.SickLeaveService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SickLeaveServiceImpl implements SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;

    private final ModelMapperUtil mapperUtil;

    @Override
    public List<SickLeave> getSickLeaves() {
        return List.of();
    }

    @Override
    public SickLeave getSickLeave(long id) {
        return null;
    }

    @Override
    public SickLeave createSickLeave(SickLeave SickLeave) {
        return null;
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

    @Override
    public void deleteSickLeave(long id) {

    }
}
