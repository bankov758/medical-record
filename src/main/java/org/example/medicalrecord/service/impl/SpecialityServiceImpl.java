package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.entity.Speciality;
import org.example.medicalrecord.repository.SpecialityRepository;
import org.example.medicalrecord.service.SpecialityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {

    private SpecialityRepository specialityRepository;

    @Override
    public List<Speciality> getDoctorsSpecialities(Long doctorId) {
        return specialityRepository.findAllByDoctors_Id(doctorId);
    }
}
