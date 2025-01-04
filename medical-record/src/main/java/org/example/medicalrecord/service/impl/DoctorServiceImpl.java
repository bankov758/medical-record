package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository DoctorRepository;

    private final ModelMapper mapper;

    @Override
    public List<Doctor> getDoctors() {
        return DoctorRepository.findAll();
    }

    @Override
    public Doctor getDoctor(long id) {
        return DoctorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Doctor.class, "id", id));
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return DoctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor, long id) {
        return this.DoctorRepository.findById(id)
                .map(Doctor1 -> {
                    mapper.map(doctor, Doctor1);
                    return this.DoctorRepository.save(Doctor1);
                }).orElseGet(() ->
                        this.DoctorRepository.save(doctor)
                );
    }

    @Override
    public void deleteDoctor(long id) {
        DoctorRepository.deleteById(id);
    }
    
}
