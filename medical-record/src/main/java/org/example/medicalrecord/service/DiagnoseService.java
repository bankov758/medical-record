package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.entity.Diagnose;

import java.util.List;

public interface DiagnoseService {

    List<Diagnose> getDiagnoses();

    Diagnose getDiagnose(long id);

    Diagnose createDiagnose(Diagnose diagnose);

    Diagnose updateDiagnose(DiagnoseDto diagnoseDto, long id);

    void deleteDiagnose(long id);

}
