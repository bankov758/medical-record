package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.DoctorDto;
import org.example.medicalrecord.exceptions.EntityNotFoundException;

import java.util.List;

public interface DoctorService {

    List<DoctorDto> getDoctors();

    List<DoctorDto> getGps();

    DoctorDto getDoctor(long id);

    DoctorDto createDoctor(DoctorDto doctorDto);

    DoctorDto updateDoctor(DoctorDto doctorDto, long id);

    void addSpeciality(String speciality, long doctorId) throws EntityNotFoundException;

    void removeSpeciality(long specialityId, long doctorId);

    void deleteDoctor(long id);
    
}
