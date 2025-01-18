package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.data.dto.UserSignupDto;
import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.User;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.repository.RoleRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ModelMapperUtil mapperUtil;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private PatientRepository patientRepository;

    private DoctorRepository doctorRepository;

    @Override
    public UserDto getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            User loggedInUser = (User) authentication.getPrincipal();
            return mapperUtil.getModelMapper().map(loggedInUser, UserDto.class);
        }
        return null;
    }

    @Override
    public User registerUser(UserSignupDto userSignupDto) {
        if (userSignupDto.getIsDoctor()){
            Doctor doctor = mapperUtil.getModelMapper().map(userSignupDto, Doctor.class);
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            Role userRole = getUserRole(Roles.ROLE_DOCTOR);
            setUpAccount(userRole, doctor);
            return doctorRepository.save(doctor);
        }
        Patient patient = mapperUtil.getModelMapper().map(userSignupDto, Patient.class);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        Role userRole = getUserRole(Roles.ROLE_PATIENT);
        setUpAccount(userRole, patient);
        return patientRepository.save(patient);
    }

    private Role getUserRole(Roles role) {
        return roleRepository.findByAuthority(role)
                .orElseThrow(() -> new EntityNotFoundException(Patient.class, "name", role.name()));
    }

    private static <U extends User> void setUpAccount(Role userRole, U user) {
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        user.setAuthorities(authorities);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
    }

}
