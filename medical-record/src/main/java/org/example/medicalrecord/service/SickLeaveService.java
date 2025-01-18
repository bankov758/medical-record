package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.entity.SickLeave;

public interface SickLeaveService {

    String getMonthWithMostSickLeaves();

    SickLeave updateSickLeave(SickLeaveDto sickLeaveDto, long id);

}
