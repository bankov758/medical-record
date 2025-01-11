package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.RecordService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.RecordDoctorViewModel;
import org.example.medicalrecord.web.view.model.RecordViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    private final DoctorService doctorService;

    private final ModelMapperUtil mapperUtil;

    private final AuthenticationService authService;

    @GetMapping
    public String getDoctors(Model model) {
        List<RecordViewModel> records = mapperUtil.mapList(recordService.getRecords(), RecordViewModel.class);
        model.addAttribute("records", records);
        return "records";
    }

    @GetMapping("/create-record")
    public String showCreateRecordForm(Model model) {
        RecordViewModel recordToBeCreated = new RecordViewModel();
        recordToBeCreated.setDoctorId(authService.getLoggedInUser().getId());
        model.addAttribute("loggedInId", recordToBeCreated);
        model.addAttribute("record", recordToBeCreated);
        model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
        return "create-record";
    }

    @PostMapping("/create")
    public String createRecord(@Valid @ModelAttribute("record") RecordViewModel record,
                               BindingResult errors,
                               Model model) {
        if (errors.hasErrors()) {
            record.setDoctorId(authService.getLoggedInUser().getId());
            model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
            return "create-record";
        }
        try {
            recordService.createRecord(mapperUtil.getModelMapper().map(record, RecordDto.class));
            return "redirect:/records";
        } catch (EntityNotFoundException ex) {
            String[] exceptionMessage = ex.getMessage().split(" ");
            String fieldName = exceptionMessage[2];
            errors.rejectValue(fieldName, "record_error", ex.getMessage());
            record.setDoctorId(authService.getLoggedInUser().getId());
            model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
            return "create-record";
        }

    }

}
