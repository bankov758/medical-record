package org.example.medicalrecord.service;

import org.example.medicalrecord.data.entity.Speciality;

import java.util.List;

public interface SpecialityService {

    List<Speciality> getDoctorsSpecialities(Long doctorId);

}
