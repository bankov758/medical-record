package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.data.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PatientService {

    List<PatientDto> getPatients();

    PatientDto getPatient(long id);

    List<PatientDto> filterPatients(Specification<Patient> specification);

    Patient createPatient(Patient patient);

    Patient updatePatient(PatientDto patientDto, long id);

    void deletePatient(long id);
    
}
