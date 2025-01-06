package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.data.entity.Patient;

import java.util.List;

public interface PatientService {

    List<PatientDto> getPatients();

    PatientDto getPatient(long id);

    Patient createPatient(Patient patient);

    Patient updatePatient(PatientDto patientDto, long id);

    void deletePatient(long id);
    
}
