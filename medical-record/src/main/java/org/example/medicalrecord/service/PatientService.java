package org.example.medicalrecord.service;

import org.example.medicalrecord.data.entity.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getPatients();

    Patient getPatient(long id);

    Patient createPatient(Patient patient);

    Patient updatePatient(Patient patient);

    void deletePatient(long id);
    
}
