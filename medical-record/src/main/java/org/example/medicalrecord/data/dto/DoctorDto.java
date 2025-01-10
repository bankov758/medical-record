package org.example.medicalrecord.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DoctorDto extends UserDto {

    @NotBlank
    @Size(min = 3, max = 20, message="Last name must be between 3 and 20 characters")
    private String username;

    private Boolean isGp;

    private Set<SpecialityDto> specialities;

}
