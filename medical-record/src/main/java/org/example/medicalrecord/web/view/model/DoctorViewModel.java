package org.example.medicalrecord.web.view.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorViewModel {

    private String firstName;

    private String lastName;

    private String username;

    private boolean isGp;

    private Set<String> specialities;

    private Set<String> authorities;

}
