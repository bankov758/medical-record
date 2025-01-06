package org.example.medicalrecord.web.view.model;

import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DoctorViewModel extends UserViewModel {

    private Boolean isGp;

    private Set<String> specialities;

}
