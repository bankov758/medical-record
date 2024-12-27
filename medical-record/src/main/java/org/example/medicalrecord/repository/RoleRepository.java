package org.example.medicalrecord.repository;



import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(Roles authority);
}
