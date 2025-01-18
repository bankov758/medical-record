package org.example.medicalrecord.web.view.controller;

import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.service.DiagnoseService;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.SickLeaveService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.DiagnoseViewModel;
import org.example.medicalrecord.web.view.model.DoctorViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final DoctorService doctorService;

    private final DiagnoseService diagnoseService;

    private final SickLeaveService sickLeaveService;

    private final ModelMapperUtil mapperUtil;

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("doctors", mapperUtil.mapList(doctorService.getTopDoctors(), DoctorViewModel.class));
        model.addAttribute("diagnoses", mapperUtil.mapList(diagnoseService.getTopDiagnoses(), DiagnoseViewModel.class));
        model.addAttribute("sickMonthMsg", sickLeaveService.getMonthWithMostSickLeaves());
        return "/index";
    }

}
