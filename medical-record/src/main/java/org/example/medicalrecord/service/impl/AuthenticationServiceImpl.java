package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.web.view.model.SignupViewModel;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.User;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.repository.RoleRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.PatientService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ModelMapperUtil mapper;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private PatientService patientService;

    @Override
    public User registerUser(SignupViewModel signupViewModel) {
        Patient patient = mapper.getModelMapper().map(signupViewModel, Patient.class);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        Role userRole = roleRepository.findByAuthority(Roles.PATIENT).get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        patient.setAuthorities(authorities);
        patient.setEnabled(true);
        patient.setAccountNonExpired(true);
        patient.setAccountNonLocked(true);
        patient.setCredentialsNonExpired(true);

        return patientService.createPatient(patient);
    }

}
