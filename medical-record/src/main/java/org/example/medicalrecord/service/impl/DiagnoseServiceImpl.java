package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.entity.Diagnose;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DiagnoseRepository;
import org.example.medicalrecord.service.DiagnoseService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiagnoseServiceImpl implements DiagnoseService {

    private final DiagnoseRepository diagnoseRepository;

    private final ModelMapperUtil mapperUtil;

    @Override
    public List<DiagnoseDto> getDiagnoses() {
        return mapperUtil.mapList(diagnoseRepository.findAll(), DiagnoseDto.class);
    }

    @Override
    public List<DiagnoseDto> getTopDiagnoses() {
        return diagnoseRepository.findTop3DiagnosesByNameCount();
    }

    @Override
    public DiagnoseDto getDiagnose(long id) {
        return mapperUtil.getModelMapper().map(fetchDiagnose(id),
                DiagnoseDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public DiagnoseDto updateDiagnose(DiagnoseDto diagnoseDto, long id) {
        Diagnose diagnose = fetchDiagnose(id);
        if (diagnose == null) {
            diagnose = new Diagnose();
        }
        diagnose.setDiagnoseName(diagnoseDto.getDiagnoseName());
        diagnose.setReceipt(diagnoseDto.getReceipt());
        diagnose.setRecord(diagnoseDto.getRecord());
        return mapperUtil.getModelMapper().map(diagnoseRepository.save(diagnose), DiagnoseDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteDiagnose(long id) {
        diagnoseRepository.deleteById(id);
    }

    private Diagnose fetchDiagnose(long id) {
        return diagnoseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Diagnose.class, "id", id));
    }
}
