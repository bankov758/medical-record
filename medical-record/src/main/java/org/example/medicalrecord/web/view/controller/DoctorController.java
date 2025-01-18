package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.dto.DoctorDto;
import org.example.medicalrecord.data.dto.SpecialityDto;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.SpecialityService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.example.medicalrecord.web.view.model.SpecialityViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    private final SpecialityService specialityService;

    private final ModelMapperUtil mapperUtil;

    @GetMapping
    public String getDoctors(Model model) {
        List<DoctorViewModel> doctors = mapperUtil.mapList(doctorService.getDoctors(), DoctorViewModel.class);
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping("/edit-doctor/{id}")
    public String showEditDoctorForm(Model model, @PathVariable Long id) {
        model.addAttribute("doctor", mapperUtil.getModelMapper()
                .map(doctorService.getDoctor(id), DoctorViewModel.class));
        model.addAttribute("speciality", new SpecialityViewModel(id));
        return "doctor-profile";
    }

    @PostMapping("/update/{id}")
    public String updateDoctor(@PathVariable Long id,
                               @Valid @ModelAttribute("doctor") DoctorViewModel doctorViewModel,
                               BindingResult errors,
                               Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("speciality", new SpecialityViewModel(id));
            doctorViewModel.setSpecialities(new HashSet<>(mapperUtil.mapList(specialityService.getDoctorsSpecialities(id), SpecialityDto.class)));
            return "doctor-profile";
        }
        doctorService.updateDoctor(mapperUtil.getModelMapper().map(doctorViewModel, DoctorDto.class), id);
        return "redirect:/doctors";
    }

    @PostMapping("/{id}/add-speciality")
    public String addSpeciality(@PathVariable Long id,
                                @Valid @ModelAttribute("speciality") SpecialityViewModel specialityViewModel,
                                BindingResult errors,
                                Model model) {
        try {
            doctorService.addSpeciality(specialityViewModel.getSpecialityName(), id);
            return "redirect:/doctors/edit-doctor/" + id;
        } catch (EntityNotFoundException ex) {
            String[] exceptionMessage = ex.getMessage().split(" ");
            String fieldName = exceptionMessage[2];
            errors.rejectValue(fieldName, "speciality_error", ex.getMessage());
            model.addAttribute("doctor", mapperUtil.getModelMapper()
                    .map(doctorService.getDoctor(id), DoctorViewModel.class));
            return "doctor-profile";
        }
    }

    @GetMapping("{id}/remove-speciality/{specialityId}")
    public String removeSpeciality(@PathVariable Long id, @PathVariable Long specialityId) {
        doctorService.removeSpeciality(specialityId, id);
        return "redirect:/doctors/edit-doctor/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }

}
