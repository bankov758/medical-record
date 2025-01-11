package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.data.entity.*;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.repository.RecordRepository;
import org.example.medicalrecord.service.DiagnoseService;
import org.example.medicalrecord.service.RecordService;
import org.example.medicalrecord.service.SickLeaveService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final DiagnoseService diagnoseService;

    private final SickLeaveService sickLeaveService;

    private final ModelMapperUtil mapperUtil;

    @Override
    public List<RecordDto> getRecords() {
        return mapperUtil.mapList(recordRepository.findAll(), RecordDto.class);
    }

    @Override
    public RecordDto getRecord(long id) {
        return mapperUtil.getModelMapper().map(fetchRecord(id), RecordDto.class);
    }

    private Record fetchRecord(long id) {
        return recordRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Record.class, "id", id));
    }

    @Override
    public RecordDto createRecord(RecordDto recordDto) {
        Record record = mapperUtil.getModelMapper().map(recordDto, Record.class);
        record.setDoctor(fetchDoctor(recordDto));
        record.setPatient(fetchPatient(recordDto));

        record.setDiagnose(new Diagnose(recordDto.getDiagnoseName(), recordDto.getReceipt(), record));
        if (recordDto.getStartDate() != null){
            record.setSickLeave(new SickLeave(recordDto.getStartDate(), recordDto.getLeaveDays(), record));
        }

        return mapperUtil.getModelMapper().map(
                recordRepository.save(record),
                RecordDto.class);
    }

    @Override
    public RecordDto updateRecord(RecordDto recordDto, long id) {
        Record record = fetchRecord(id);
        if (recordDto.getVisitDate() != null){
            record.setVisitDate(recordDto.getVisitDate());
        }
        diagnoseService.updateDiagnose(new DiagnoseDto(recordDto.getDiagnoseName(), recordDto.getReceipt(), record), id);
        sickLeaveService.updateSickLeave(new SickLeaveDto(recordDto.getStartDate(), recordDto.getLeaveDays(), record), id);
        record.setDoctor(fetchDoctor(recordDto));
        record.setPatient(fetchPatient(recordDto));
        return mapperUtil.getModelMapper().map(recordRepository.save(record), RecordDto.class);
    }

    @Override
    public void deleteRecord(long id) {
        recordRepository.deleteById(id);
    }

    private Doctor fetchDoctor(RecordDto recordDto) {
        return doctorRepository.findById(recordDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor wth doctorId " + recordDto.getDoctorId() + " was not found"));
    }

    private Patient fetchPatient(RecordDto recordDto) {
        return patientRepository.findByEgn(recordDto.getPatientEgn())
                .orElseThrow(() -> new EntityNotFoundException("Patient wth patientEgn " + recordDto.getPatientEgn() + " was not found"));
    }
}
