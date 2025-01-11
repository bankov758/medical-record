package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.entity.Diagnose;
import org.example.medicalrecord.repository.DiagnoseRepository;
import org.example.medicalrecord.service.DiagnoseService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiagnoseServiceImpl implements DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;

    private final ModelMapperUtil mapperUtil;

    @Override
    public List<Diagnose> getDiagnoses() {
        return List.of();
    }

    @Override
    public Diagnose getDiagnose(long id) {
        return null;
    }

    @Override
    public Diagnose createDiagnose(Diagnose diagnose) {
        return null;
    }

    @Override
    public Diagnose updateDiagnose(DiagnoseDto diagnoseDto, long id) {
        Diagnose diagnose = diagnoseRepository.findByRecordId(id);
        if (diagnose == null) {
            diagnose = new Diagnose();
        }
        diagnose.setDiagnoseName(diagnoseDto.getDiagnoseName());
        diagnose.setReceipt(diagnoseDto.getReceipt());
        diagnose.setRecord(diagnoseDto.getRecord());
        return diagnoseRepository.save(diagnose);
    }

    @Override
    public void deleteDiagnose(long id) {

    }
}
