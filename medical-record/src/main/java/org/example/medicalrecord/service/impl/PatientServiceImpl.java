package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.service.PatientService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    private DoctorRepository doctorRepository;

    private final ModelMapperUtil mapperUtil;

    @Override
    public List<PatientDto> getPatients() {
        return mapperUtil.mapList(patientRepository.findAll(), PatientDto.class);
    }

    @Override
    public PatientDto getPatient(long id) {
        return mapperUtil.getModelMapper().map(
                patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Patient.class, "id", id)),
                PatientDto.class);
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(PatientDto patientDto, long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Patient.class, "id", id));
        mapperUtil.getModelMapper().map(patientDto, patient);
        if (patientDto.getGpId() != 0){
            patient.setGp(doctorRepository.findById(patientDto.getGpId())
                    .orElseThrow(() -> new EntityNotFoundException(Doctor.class, "id", patientDto.getGpId())));
        }
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(long id) {
        patientRepository.deleteById(id);
    }
    
}
