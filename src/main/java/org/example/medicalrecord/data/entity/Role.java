package org.example.medicalrecord.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.medicalrecord.data.enums.Roles;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles authority;

    @JsonBackReference
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER)
    private Set<User> users;

    @Override
    public String getAuthority() {
        return authority.name();
    }

    public Role(Roles authority) {
        this.authority = authority;
    }
}
