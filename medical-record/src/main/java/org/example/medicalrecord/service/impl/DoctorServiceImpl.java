package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    private final ModelMapperUtil mapperUtil;

    @Override
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getGps() {
        return doctorRepository.findAllByIsGpTrue();
    }

    @Override
    public Doctor getDoctor(long id) {
        return doctorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Doctor.class, "id", id));
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor, long id) {
        return this.doctorRepository.findById(id)
                .map(Doctor1 -> {
                    mapperUtil.getModelMapper().map(doctor, Doctor1);
                    return this.doctorRepository.save(Doctor1);
                }).orElseGet(() ->
                        this.doctorRepository.save(doctor)
                );
    }

    @Override
    public void deleteDoctor(long id) {
        doctorRepository.deleteById(id);
    }
    
}
