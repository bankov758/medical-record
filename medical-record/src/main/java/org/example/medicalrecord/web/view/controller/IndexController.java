package org.example.medicalrecord.web.view.controller;

import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final DoctorService doctorService;

    private final ModelMapperUtil mapperUtil;

    @GetMapping("/")
    public String getIndex(Model model) {

        List<DoctorViewModel> doctors = mapperUtil
                .mapList(doctorService.getTopDoctors(), DoctorViewModel.class);
        model.addAttribute("doctors", doctors);
        return "/index";
    }

}
