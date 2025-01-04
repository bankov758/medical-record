package org.example.medicalrecord.web.view.controller;

import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.service.PatientService;
import org.example.medicalrecord.service.UserService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.example.medicalrecord.web.view.model.PatientsViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class PatientController {

    private final PatientService patientService;

    private final ModelMapperUtil mapperUtil;

    @GetMapping
    public String getPatients(Model model) {
        List<PatientsViewModel> patients = mapperUtil.mapList(patientService.getPatients(), PatientsViewModel.class);
        model.addAttribute("patients", patients);
        return "patients";
    }

}
