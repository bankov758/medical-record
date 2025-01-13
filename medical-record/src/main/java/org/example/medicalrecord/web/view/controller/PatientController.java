package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.repository.specification.PatientSpecification;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.PatientService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.example.medicalrecord.web.view.model.PatientSearchModel;
import org.example.medicalrecord.web.view.model.PatientViewModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.medicalrecord.util.DataUtil.localDateToDate;

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
        model.addAttribute("searchPatient", new PatientSearchModel());
        model.addAttribute("gps", mapperUtil.mapList(doctorService.getGps(), DoctorViewModel.class));
        return "patients";
    }

    @PostMapping("/filter")
    public String getFilteredPatients(Model model, @ModelAttribute("searchPatient") PatientSearchModel searchPatient) {
        Specification<Patient> spec = PatientSpecification.filterRecords(
                searchPatient.getFirstName(),
                searchPatient.getLastName(),
                searchPatient.getEgn(),
                searchPatient.getGpId(),
                localDateToDate(searchPatient.getLastPaidMedicalFrom()),
                localDateToDate(searchPatient.getLastPaidMedicalTo()));
        List<PatientViewModel> patients = mapperUtil.mapList(patientService.filterPatients(spec), PatientViewModel.class);
        model.addAttribute("patients", patients);
        model.addAttribute("gps", mapperUtil.mapList(doctorService.getGps(), DoctorViewModel.class));
        return "patients";
    }

    @GetMapping("/edit-patient/{id}")
    public String showEditPatientForm(Model model, @PathVariable Long id) {
        model.addAttribute("patient", mapperUtil.getModelMapper()
                .map(patientService.getPatient(id), PatientViewModel.class));
        model.addAttribute("gps", mapperUtil.mapList(doctorService.getGps(), DoctorViewModel.class));
        return "patient-profile";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable Long id,
                                @Valid @ModelAttribute("patient") PatientViewModel patientViewModel,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("gps", mapperUtil.mapList(doctorService.getGps(), DoctorViewModel.class));;
            return "patient-profile";
        }
        patientService.updatePatient(mapperUtil.getModelMapper()
                .map(patientViewModel, PatientDto.class), id);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }

}
