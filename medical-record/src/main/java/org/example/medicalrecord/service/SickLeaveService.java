package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.entity.SickLeave;

import java.util.List;

public interface SickLeaveService {

    List<SickLeave> getSickLeaves();

    SickLeave getSickLeave(long id);

    SickLeave createSickLeave(SickLeave sickLeave);

    SickLeave updateSickLeave(SickLeaveDto sickLeaveDto, long id);

    void deleteSickLeave(long id);

}
