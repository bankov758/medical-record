package org.example.medicalrecord.configuration;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                //.requestMatchers(HttpMethod.GET, "/medicines").hasAuthority("CUSTOMER")

                        .requestMatchers("/doctors").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/doctors/edit-doctor/*").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/doctors/update/*").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/doctors/*/add-speciality").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/doctors/*/remove-speciality").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/doctors/delete/*").hasAuthority(Roles.ROLE_ADMIN.name())

                        .requestMatchers("/patients").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/patients/filter").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name(), Roles.ROLE_PATIENT.name())
                        .requestMatchers("/patients/edit-patient/*").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name(), Roles.ROLE_PATIENT.name())
                        .requestMatchers("/patients/update/*").hasAnyAuthority(Roles.ROLE_ADMIN.name(), Roles.ROLE_PATIENT.name())
                        .requestMatchers("/patients/delete/*").hasAuthority(Roles.ROLE_ADMIN.name())

                        .requestMatchers("/records").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/records/filter").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/records/create-record").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/records/create").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/records/edit-record/*").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/records/update/*").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())
                        .requestMatchers("/records/delete/*").hasAnyAuthority(Roles.ROLE_DOCTOR.name(), Roles.ROLE_ADMIN.name())

                        .requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/").permitAll()

                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }

}
