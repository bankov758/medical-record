package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.DiagnoseDto;

import java.util.List;

public interface DiagnoseService {

    List<DiagnoseDto> getDiagnoses();

    List<DiagnoseDto> getTopDiagnoses();

    DiagnoseDto getDiagnose(long id);

    DiagnoseDto updateDiagnose(DiagnoseDto diagnoseDto, long id);

    void deleteDiagnose(long id);

}
