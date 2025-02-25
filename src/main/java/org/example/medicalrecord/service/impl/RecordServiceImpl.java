package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.data.entity.*;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.exceptions.AuthorizationFailureException;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.repository.RecordRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.DiagnoseService;
import org.example.medicalrecord.service.RecordService;
import org.example.medicalrecord.service.SickLeaveService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final DiagnoseService diagnoseService;

    private final SickLeaveService sickLeaveService;

    private final ModelMapperUtil mapperUtil;

    private final AuthenticationService authenticationService;

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN', 'ROLE_PATIENT')")
    public List<RecordDto> getRecords() {
        UserDto loggedInUser = authenticationService.getLoggedInUser();
        if (loggedInUser.getAuthorities().contains(Roles.ROLE_DOCTOR.name()) || loggedInUser.getAuthorities().contains(Roles.ROLE_ADMIN.name())) {
            return mapperUtil.mapList(recordRepository.findAll(), RecordDto.class);
        }
        return mapperUtil.mapList(recordRepository.findAllByPatientId(loggedInUser.getId()), RecordDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN', 'ROLE_PATIENT')")
    public RecordDto getRecord(long id) {
        return mapperUtil.getModelMapper().map(fetchRecord(id), RecordDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN', 'ROLE_PATIENT')")
    public List<RecordDto> filterRecords(Specification<Record> specification) {
        UserDto loggedInUser = authenticationService.getLoggedInUser();
        List<Record> result = recordRepository.findAll(specification);
        if (loggedInUser.getAuthorities().contains(Roles.ROLE_DOCTOR.name()) || loggedInUser.getAuthorities().contains(Roles.ROLE_ADMIN.name())) {
            return mapperUtil.mapList(result, RecordDto.class);
        }
        result = result.stream()
                .filter(record -> record.getPatient().getId() == loggedInUser.getId())
                .collect(Collectors.toList());
        return mapperUtil.mapList(result, RecordDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public RecordDto updateRecord(RecordDto recordDto, long id) {
        Record record = fetchRecord(id);
        UserDto loggedInUser = authenticationService.getLoggedInUser();
        if (record.getDoctor().getId() != loggedInUser.getId() && !loggedInUser.getAuthorities().contains(Roles.ROLE_ADMIN.name())) {
            throw new AuthorizationFailureException("You are not authorized to update this record");
        }
        if (recordDto.getVisitDate() != null) {
            record.setVisitDate(recordDto.getVisitDate());
        }
        diagnoseService.updateDiagnose(new DiagnoseDto(recordDto.getDiagnoseName(), recordDto.getReceipt(), record), id);
        if (recordDto.getStartDate() != null && recordDto.getLeaveDays() != 0) {
            if (record.getSickLeave() == null) {
                record.setSickLeave(new SickLeave(recordDto.getStartDate(), recordDto.getLeaveDays(), record));
            } else {
                sickLeaveService.updateSickLeave(new SickLeaveDto(recordDto.getStartDate(), recordDto.getLeaveDays(), record), id);
            }
        }
        record.setDoctor(fetchDoctor(recordDto));
        record.setPatient(fetchPatient(recordDto));
        return mapperUtil.getModelMapper().map(recordRepository.save(record), RecordDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public void deleteRecord(long id) {
        recordRepository.deleteById(id);
    }

    private Record fetchRecord(long id) {
        return recordRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Record.class, "id", id));
    }

    private Doctor fetchDoctor(RecordDto recordDto) {
        return doctorRepository.findById(recordDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor wth doctorId " + recordDto.getDoctorId() + " was not found"));
    }

    private Patient fetchPatient(RecordDto recordDto) {
        return patientRepository.findByEgnAndFirstNameAndLastName(recordDto.getPatientEgn(), recordDto.getPatientFirstName(), recordDto.getPatientLastName())
                .orElseThrow(() -> new EntityNotFoundException("Patient wth this egn name or last name was not found"));
    }
}
