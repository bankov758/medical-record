package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.PatientService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.example.medicalrecord.web.view.model.PatientViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final ModelMapperUtil mapperUtil;

    @GetMapping
    public String getPatients(Model model) {
        List<PatientViewModel> patients = mapperUtil.mapList(patientService.getPatients(), PatientViewModel.class);
        model.addAttribute("patients", patients);
        return "patients";
    }

    @GetMapping("/{id}")
    public String getPatient(@PathVariable long id, Model model){
        model.addAttribute("patients", mapperUtil.getModelMapper()
                .map(patientService.getPatient(id), PatientViewModel.class));
        return "patient-profile";
    }

    @GetMapping("/edit-patient/{id}")
    public String showEditPatientForm(Model model, @PathVariable Long id) {
        model.addAttribute("patient", mapperUtil.getModelMapper()
                .map(patientService.getPatient(id), PatientViewModel.class));
        model.addAttribute("gps", mapperUtil.mapList(doctorService.getGps(), DoctorViewModel.class));
        return "patient-profile";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable Long id, @Valid @ModelAttribute("patient") PatientViewModel patientViewModel) {
        patientService.updatePatient(mapperUtil.getModelMapper()
                .map(patientViewModel, PatientDto.class), id);
        return "redirect:/patients";
    }
}
