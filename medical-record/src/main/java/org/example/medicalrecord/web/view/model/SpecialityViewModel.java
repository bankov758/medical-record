package org.example.medicalrecord.web.view.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpecialityViewModel {

    @NotBlank
    @Size(min = 3, max = 20, message="First name must be between 3 and 20 characters")
    private String specialityName;

    private long doctorId;

    public SpecialityViewModel(long doctorId) {
        this.doctorId = doctorId;
    }
}
