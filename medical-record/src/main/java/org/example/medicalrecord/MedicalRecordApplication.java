package org.example.medicalrecord;

import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.User;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.repository.RoleRepository;
import org.example.medicalrecord.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MedicalRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalRecordApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode) {
        return args -> {
            if (roleRepository.findByAuthority(Roles.ADMIN).isPresent()) return;
            Role adminRole = roleRepository.save(new Role(Roles.ADMIN));
            roleRepository.save(new Role(Roles.DOCTOR));
            roleRepository.save(new Role(Roles.PATIENT));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            User admin = new User("adminFirstName", "adminLastName",
                    "admin", passwordEncode.encode("nimda"), roles);

            userRepository.save(admin);
        };
    }

}
