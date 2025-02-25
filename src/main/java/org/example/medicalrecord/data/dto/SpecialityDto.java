package org.example.medicalrecord.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpecialityDto {

    private long id;

    @NotBlank
    @Size(min = 3, max = 20, message="First name must be between 3 and 20 characters")
    private String specialtyName;

}
