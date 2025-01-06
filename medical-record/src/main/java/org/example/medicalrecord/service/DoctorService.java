package org.example.medicalrecord.service;

import org.example.medicalrecord.data.entity.Doctor;

import java.util.List;

public interface DoctorService {

    List<Doctor> getDoctors();

    List<Doctor> getGps();

    Doctor getDoctor(long id);

    Doctor createDoctor(Doctor doctor);

    Doctor updateDoctor(Doctor doctor, long id);

    void deleteDoctor(long id);
    
}
