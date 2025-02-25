package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.DoctorDto;
import org.example.medicalrecord.data.dto.SpecialityDto;
import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.data.entity.Speciality;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.exceptions.AuthorizationFailureException;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.SpecialityRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    private SpecialityRepository specialityRepository;

    private final AuthenticationService authenticationService;

    private final ModelMapperUtil mapperUtil;

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public List<DoctorDto> getDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> {
                    DoctorDto doctorDto = mapperUtil.getModelMapper().map(doctor, DoctorDto.class);
                    doctorDto.setNumberOfVisits(doctorRepository.countByRecordsDoctorId(doctor.getId()));
                    doctorDto.setNumberOfPatients(doctorRepository.countByPatientsGpId(doctor.getId()));
                    return doctorDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDto> getTopDoctors() {
        return doctorRepository.findTop3ByOrderByRecordsSickLeaveDesc()
                .stream()
                .map(doctor -> {
                    DoctorDto doctorDto = mapperUtil.getModelMapper().map(doctor, DoctorDto.class);
                    doctorDto.setNumberOfVisits(doctorRepository.countByRecordsDoctorId(doctor.getId()));
                    doctorDto.setNumberOfPatients(doctorRepository.countByPatientsGpId(doctor.getId()));
                    doctorDto.setNumberOfSickLeaves(doctorRepository.countByIdAndRecordsSickLeaveIsNotNull(doctor.getId()));
                    return doctorDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDto> getGps() {
        return mapperUtil.mapList(doctorRepository.findAllByIsGpTrue(), DoctorDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public DoctorDto getDoctor(long id) {
        DoctorDto doctorDto = mapperUtil.getModelMapper().map(fetchDoctor(id), DoctorDto.class);
        doctorDto.setNumberOfVisits(doctorRepository.countByRecordsDoctorId(id));
        doctorDto.setNumberOfPatients(doctorRepository.countByPatientsGpId(id));
        return doctorDto;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        return mapperUtil.getModelMapper().map(
                doctorRepository.save(mapperUtil.getModelMapper().map(doctorDto, Doctor.class)),
                DoctorDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public DoctorDto updateDoctor(DoctorDto doctorDto, long id) {
        Doctor doctor = fetchDoctor(id);
        UserDto loggedInUser = authenticationService.getLoggedInUser();
        if (doctor.getId() != loggedInUser.getId() && !loggedInUser.getAuthorities().contains(Roles.ROLE_ADMIN.name())) {
            throw new AuthorizationFailureException("You are not authorized to update this record");
        }
        mapperUtil.getModelMapper()
                .typeMap(DoctorDto.class, Doctor.class)
                .addMappings(mapper -> mapper.skip(Doctor::setSpecialities))
                .map(doctorDto, doctor);
        if (doctorDto.getSpecialities() != null && !doctorDto.getSpecialities().isEmpty()) {
            for (SpecialityDto speciality : doctorDto.getSpecialities()) {
                if (!doctorRepository.existsByIdAndSpecialitiesSpecialtyName(id, speciality.getSpecialtyName())) {
                    Speciality newSpeciality = specialityRepository.findBySpecialtyName(speciality.getSpecialtyName())
                            .orElseThrow(() -> new EntityNotFoundException(Doctor.class, "id", id));
                    doctor.getSpecialities().add(newSpeciality);
                }
            }
        }
        return mapperUtil.getModelMapper().map(doctorRepository.save(doctor), DoctorDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addSpeciality(String speciality, long doctorId) throws EntityNotFoundException {
        Doctor doctor = fetchDoctor(doctorId);
        if (!doctorRepository.existsByIdAndSpecialitiesSpecialtyName(doctorId, speciality)) {
            Speciality newSpeciality = specialityRepository.findBySpecialtyName(speciality)
                    .orElseThrow(() -> new EntityNotFoundException(Speciality.class, "specialityName", speciality));
            doctor.getSpecialities().add(newSpeciality);
            doctorRepository.save(doctor);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void removeSpeciality(long specialityId, long doctorId) {
        Doctor doctor = fetchDoctor(doctorId);
        if (doctorRepository.existsByIdAndSpecialitiesId(doctorId, specialityId)){
            doctor.getSpecialities().remove(specialityRepository.findById(specialityId)
                    .orElseThrow(() -> new EntityNotFoundException(Speciality.class, "id", specialityId)));
            doctorRepository.save(doctor);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteDoctor(long id) {
        doctorRepository.deleteById(id);
    }

    private Doctor fetchDoctor(long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Doctor.class, "id", id));
    }
    
}
