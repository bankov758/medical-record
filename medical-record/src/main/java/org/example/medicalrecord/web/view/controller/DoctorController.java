package org.example.medicalrecord.web.view.controller;

import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.entity.Speciality;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    private final ModelMapperUtil mapperUtil;

    @GetMapping
    public String getDoctors(Model model) {
        List<DoctorViewModel> doctors = mapperUtil.mapList(doctorService.getDoctors(), DoctorViewModel.class);
        model.addAttribute("doctors", doctors);
        return "doctors";
    }
}
