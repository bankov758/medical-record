package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.entity.Diagnose;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.data.entity.SickLeave;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.*;
import org.example.medicalrecord.service.RecordService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final DiagnoseRepository diagnoseRepository;

    private final SickLeaveRepository sickleaveRepository;

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
        record.setDoctor(doctorRepository.findById(recordDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor wth doctorId " + recordDto.getDoctorId() + " was not found")));
        record.setPatient(patientRepository.findByEgn(recordDto.getPatientEgn())
                .orElseThrow(() -> new EntityNotFoundException("Patient wth patientEgn " + recordDto.getPatientEgn() + " was not found")));

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
        mapperUtil.getModelMapper()
//                .typeMap(DoctorDto.class, Doctor.class)
//                .addMappings(mapper -> mapper.skip(Doctor::setSpecialities))
                .map(recordDto, record);

        return mapperUtil.getModelMapper().map(recordRepository.save(record), RecordDto.class);
    }

    @Override
    public void deleteRecord(long id) {
        recordRepository.deleteById(id);
    }

}
