package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.data.dto.UserSignupDto;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.User;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.repository.RoleRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.PatientService;
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

    private PatientService patientService;

    @Override
    public UserDto getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            User loggedInUser = (User) authentication.getPrincipal(); // Assuming your `User` implements `UserDetails`
            return mapperUtil.getModelMapper().map(loggedInUser, UserDto.class);
        }
        return null; // Or throw an exception if authentication is required
    }

    @Override
    public User registerUser(UserSignupDto userSignupDto) {
        Patient patient = mapperUtil.getModelMapper().map(userSignupDto, Patient.class);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        Role userRole = roleRepository.findByAuthority(Roles.ROLE_PATIENT).get();

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
