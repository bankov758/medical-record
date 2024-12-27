package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    private final ModelMapper mapper;

    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatient(long id) {
        return patientRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Patient.class, "id", id));
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        return this.patientRepository.findById(patient.getId())
                .map(patient1 -> {
                    mapper.map(patient, patient1);
                    return this.patientRepository.save(patient1);
                }).orElseGet(() ->
                        this.patientRepository.save(patient)
                );
    }

    @Override
    public void deletePatient(long id) {
        patientRepository.deleteById(id);
    }
    
}
