package org.example.medicalrecord.configuration;

import lombok.AllArgsConstructor;
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

                        .requestMatchers("/doctors").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/doctors/edit-doctor/*").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/doctors/update/*").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/doctors/*/add-speciality").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/doctors/*/remove-speciality").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/doctors/delete/*").hasAuthority("ADMIN")

                        .requestMatchers("/patients").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/patients/filter").hasAnyAuthority("DOCTOR", "ADMIN", "PATIENT")
                        .requestMatchers("/patients/edit-patient/*").hasAnyAuthority("DOCTOR", "ADMIN", "PATIENT")
                        .requestMatchers("/patients/update/*").hasAnyAuthority("ADMIN", "PATIENT")
                        .requestMatchers("/patients/delete/*").hasAuthority("ADMIN")

                        .requestMatchers("/records").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/records/filter").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/records/create-record").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/records/create").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/records/edit-record/*").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/records/update/*").hasAnyAuthority("DOCTOR", "ADMIN")
                        .requestMatchers("/records/delete/*").hasAnyAuthority("DOCTOR", "ADMIN")

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
