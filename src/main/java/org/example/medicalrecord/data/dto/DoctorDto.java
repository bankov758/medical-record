package org.example.medicalrecord.data.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class DoctorDto extends UserDto {

    private Boolean isGp;

    private Set<SpecialityDto> specialities;

    private Integer numberOfVisits;

    private Integer numberOfPatients;

    private Integer numberOfSickLeaves;

}
