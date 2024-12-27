package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.dto.UserLoginDto;
import org.example.medicalrecord.data.dto.UserLoginResponseDto;
import org.example.medicalrecord.data.dto.UserRegisterDto;
import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.User;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.repository.RoleRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.TokenService;
import org.example.medicalrecord.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ModelMapper mapper;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    private UserService userService;

    @Override
    public User registerUser(UserRegisterDto userRegisterDto) {
        User user = mapper.map(userRegisterDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByAuthority(Roles.PATIENT).get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        user.setAuthorities(authorities);

        return userService.createUser(user);
    }

    @Override
    public UserLoginResponseDto loginUser(UserLoginDto userLoginDto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(),
                            userLoginDto.getPassword()));
            String token = tokenService.generateJwt(auth);
            User loggedUser = (User) userService.loadUserByUsername(userLoginDto.getUsername());
            UserLoginResponseDto loginResponse = mapper.map(loggedUser, UserLoginResponseDto.class);
            loginResponse.setJwt(token);
            return loginResponse;
        } catch (AuthenticationException e) {
            return new UserLoginResponseDto();
        }
    }

}
