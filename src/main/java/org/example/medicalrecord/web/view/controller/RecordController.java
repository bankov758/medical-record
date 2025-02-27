package org.example.medicalrecord.web.view.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.specification.RecordSpecification;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.RecordService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.model.RecordDoctorViewModel;
import org.example.medicalrecord.web.view.model.RecordSearchModel;
import org.example.medicalrecord.web.view.model.RecordViewModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.medicalrecord.util.DataUtil.localDateToDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    private final DoctorService doctorService;

    private final ModelMapperUtil mapperUtil;

    private final AuthenticationService authService;

    @GetMapping
    public String getRecords(Model model) {
        List<RecordViewModel> records = mapperUtil.mapList(recordService.getRecords(), RecordViewModel.class);
        model.addAttribute("records", records);
        model.addAttribute("searchRecord", new RecordSearchModel());
        return "records";
    }

    @PostMapping("/filter")
    public String getFilteredRecords(Model model, @ModelAttribute("searchRecord") RecordSearchModel searchRecord) {
        Specification<Record> spec = RecordSpecification.filterRecords(
                searchRecord.getDoctorFirstName(),
                searchRecord.getDoctorLastName(),
                searchRecord.getPatientFirstName(),
                searchRecord.getPatientLastName(),
                searchRecord.getPatientEgn(),
                searchRecord.getDiagnoseName(),
                localDateToDate(searchRecord.getVisitDateFrom()),
                localDateToDate(searchRecord.getVisitDateTo()));
        List<RecordViewModel> records = mapperUtil.mapList(recordService.filterRecords(spec), RecordViewModel.class);
        model.addAttribute("records", records);
        return "records";
    }

    @GetMapping("/create-record")
    public String showCreateRecordForm(Model model) {
        RecordViewModel recordToBeCreated = new RecordViewModel();
        recordToBeCreated.setDoctorId(authService.getLoggedInUser().getId());
        model.addAttribute("record", recordToBeCreated);
        model.addAttribute("loggedId", authService.getLoggedInUser().getId());
        model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
        return "record-create";
    }

    @PostMapping("/create")
    public String createRecord(@Valid @ModelAttribute("record") RecordViewModel record,
                               BindingResult errors,
                               Model model) {
        if (errors.hasErrors()) {
            record.setDoctorId(authService.getLoggedInUser().getId());
            model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
            return "record-create";
        }
        recordService.createRecord(mapperUtil.getModelMapper().map(record, RecordDto.class));
        return "redirect:/records";
    }

    private void handleErrors(@ModelAttribute("record") @Valid RecordViewModel record, BindingResult errors, Model model, EntityNotFoundException ex) {
        String[] exceptionMessage = ex.getMessage().split(" ");
        String fieldName = exceptionMessage[2];
        errors.rejectValue(fieldName, "record_error", ex.getMessage());
        record.setDoctorId(authService.getLoggedInUser().getId());
        model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
    }

    @GetMapping("/edit-record/{id}")
    public String showEditRecordForm(Model model, @PathVariable Long id) {
        model.addAttribute("loggedId", authService.getLoggedInUser().getId());
        model.addAttribute("record", mapperUtil.getModelMapper().map(recordService.getRecord(id), RecordViewModel.class));
        model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
        return "record-update";
    }

    @PostMapping("/update/{id}")
    public String updateRecord(@PathVariable Long id,
                               @Valid @ModelAttribute("record") RecordViewModel record,
                               BindingResult errors,
                               Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("doctors", mapperUtil.mapList(doctorService.getDoctors(), RecordDoctorViewModel.class));
            return "record-update";
        }
        recordService.updateRecord(mapperUtil.getModelMapper().map(record, RecordDto.class), id);
        return "redirect:/records/edit-record/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return "redirect:/records";
    }

}
