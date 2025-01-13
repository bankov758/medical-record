package org.example.medicalrecord.web.view.model;

import lombok.*;
import org.example.medicalrecord.data.dto.SpecialityDto;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DoctorViewModel extends UserViewModel {

    private Boolean isGp;

    private Set<SpecialityDto> specialities;

    private Integer numberOfPatients;

    private Integer numberOfVisits;

    private Integer numberOfSickLeaves = 0;

}
