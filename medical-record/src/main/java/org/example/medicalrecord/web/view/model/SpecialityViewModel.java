package org.example.medicalrecord.web.view.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialityViewModel {

    private String specialityName;

    private long doctorId;

    public SpecialityViewModel(long doctorId) {
        this.doctorId = doctorId;
    }
}
