package org.example.medicalrecord.controller;

import org.example.medicalrecord.configuration.SecurityConfig;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.DoctorService;
import org.example.medicalrecord.service.RecordService;
import org.example.medicalrecord.service.UserService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.example.medicalrecord.web.view.controller.RecordController;
import org.example.medicalrecord.web.view.model.RecordDoctorViewModel;
import org.example.medicalrecord.web.view.model.RecordSearchModel;
import org.example.medicalrecord.web.view.model.RecordViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {RecordController.class, SecurityConfig.class})
@Import(SecurityConfig.class)
public class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordService recordService;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private ModelMapperUtil mapperUtil;

    @MockitoBean
    private AuthenticationService authService;

    @MockitoBean
    private UserService userService;

    private static final Long LOGGED_USER_ID = 1L;

    @BeforeEach
    public void setup() {
        UserDto loggedInUser = UserDto.builder().id(LOGGED_USER_ID).build();
        given(authService.getLoggedInUser()).willReturn(loggedInUser);
        given(mapperUtil.getModelMapper()).willReturn(new ModelMapper());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void getRecordsTest() throws Exception {
        RecordViewModel record1 = RecordViewModel.builder()
                .id(1L)
                .visitDate(LocalDate.parse("2024-11-11"))
                .build();

        RecordViewModel record2 = RecordViewModel.builder()
                .id(2L)
                .visitDate(LocalDate.parse("2024-11-11"))
                .build();

        List<RecordViewModel> recordsExpected = List.of(record1, record2);

        given(recordService.getRecords()).willReturn(new ArrayList<>());
        given(mapperUtil.mapList(anyList(), eq(RecordViewModel.class))).willReturn(recordsExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/records"))
                .andExpect(status().isOk())
                .andExpect(view().name("records"))
                .andExpect(model().attribute("records", recordsExpected))
                .andExpect(model().attribute("searchRecord", new RecordSearchModel()))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getFilteredRecordsTest() throws Exception {
        RecordSearchModel searchModel = RecordSearchModel.builder()
                .doctorFirstName("Doctor")
                .doctorLastName("Doctorov")
                .visitDateFrom(LocalDate.of(2024, 1, 1))
                .build();
        RecordViewModel record1 = RecordViewModel.builder()
                .id(1L)
                .visitDate(LocalDate.parse("2024-06-15"))
                .build();
        RecordViewModel record2 = RecordViewModel.builder()
                .id(2L)
                .visitDate(LocalDate.parse("2024-07-20"))
                .build();

        List<RecordViewModel> filteredRecords = List.of(record1, record2);
        given(recordService.filterRecords(any())).willReturn(new ArrayList<>());
        given(mapperUtil.mapList(anyList(), eq(RecordViewModel.class))).willReturn(filteredRecords);

        mockMvc.perform(MockMvcRequestBuilders.post("/records/filter")
                        .with(csrf())
                        .flashAttr("searchRecord", searchModel))
                .andExpect(status().isOk())
                .andExpect(view().name("records"))
                .andExpect(model().attribute("records", filteredRecords))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void showCreateRecordFormTest() throws Exception {
        RecordDoctorViewModel doctor1 = new RecordDoctorViewModel(2L, "John", "Doe");
        RecordDoctorViewModel doctor2 = new RecordDoctorViewModel(3L, "Jane", "Smith");
        List<RecordDoctorViewModel> doctors = List.of(doctor1, doctor2);

        given(doctorService.getDoctors()).willReturn(new ArrayList<>());
        given(mapperUtil.mapList(anyList(), eq(RecordDoctorViewModel.class))).willReturn(doctors);

        mockMvc.perform(MockMvcRequestBuilders.get("/records/create-record").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("record-create"))
                .andExpect(model().attributeExists("record"))
                .andExpect(model().attribute("loggedId", LOGGED_USER_ID))
                .andExpect(model().attribute("doctors", doctors))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void createValidRecordTest() throws Exception {
        RecordViewModel record = getValidMockRecordViewModel();

        mockMvc.perform(MockMvcRequestBuilders.post("/records/create")
                        .with(csrf())
                        .flashAttr("record", record))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/records"))
                .andDo(print());

        verify(recordService, times(1)).createRecord(any(RecordDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void createRecordTestWithErrors() throws Exception {
        RecordViewModel record = new RecordViewModel();

        mockMvc.perform(MockMvcRequestBuilders.post("/records/create")
                        .with(csrf())
                        .flashAttr("record", record))
                .andExpect(status().isOk())
                .andExpect(view().name("record-create"))
                .andExpect(model().attributeExists("doctors"))
                .andDo(print());

        verifyNoInteractions(recordService);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void createRecordTestEntityNotFoundException() throws Exception {
        RecordViewModel record = getValidMockRecordViewModel();

        doThrow(new EntityNotFoundException("Error field doctorId"))
                .when(recordService).createRecord(any(RecordDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/records/create")
                        .with(csrf())
                        .flashAttr("record", record))
                .andExpect(status().isOk())
                .andExpect(view().name("record-create"))
                .andExpect(model().attributeExists("doctors"))
                .andDo(print());

        verify(recordService, times(1)).createRecord(any(RecordDto.class));
    }

    @Test
    @WithMockUser(username = "doctor", authorities = {"ROLE_DOCTOR"})
    void showEditRecordFormTest() throws Exception {
        long recordId = 1L;
        RecordDto mockRecord = new RecordDto();
        mockRecord.setId(1L);

        RecordViewModel mockRecordViewModel = RecordViewModel.builder()
                .id(1L)
                .visitDate(LocalDate.parse("2024-11-11"))
                .build();
        RecordDoctorViewModel doctor1 = RecordDoctorViewModel.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .build();
        RecordDoctorViewModel doctor2 = RecordDoctorViewModel.builder()
                .id(3L)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        List<RecordDoctorViewModel> doctors = List.of(doctor1, doctor2);

        ModelMapper mockModelMapper = mock(ModelMapper.class);
        given(recordService.getRecord(recordId)).willReturn(mockRecord);
        given(mapperUtil.getModelMapper()).willReturn(mockModelMapper);
        given(mockModelMapper.map(mockRecord, RecordViewModel.class)).willReturn(mockRecordViewModel);
        given(mapperUtil.mapList(anyList(), eq(RecordDoctorViewModel.class))).willAnswer(invocation -> doctors);

        mockMvc.perform(MockMvcRequestBuilders.get("/records/edit-record/{id}", recordId))
                .andExpect(status().isOk())
                .andExpect(view().name("record-update"))
                .andExpect(model().attribute("record", mockRecordViewModel))
                .andExpect(model().attribute("doctors", doctors))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void updateRecordTestValid() throws Exception {
        Long id = 1L;
        RecordViewModel record = getValidMockRecordViewModel();

        mockMvc.perform(MockMvcRequestBuilders.post("/records/update/{id}", id)
                        .with(csrf())
                        .flashAttr("record", record))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/records/edit-record/" + id))
                .andDo(print());

        verify(recordService, times(1)).updateRecord(any(RecordDto.class), eq(id));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void updateRecordTestWithErrors() throws Exception {
        long id = 1L;

        RecordViewModel recordViewModel = RecordViewModel.builder()
                .id(id)
                .visitDate(null)
                .build();

        List<RecordDoctorViewModel> doctors = List.of(
                RecordDoctorViewModel.builder().id(2L).firstName("John").lastName("Doe").build()
        );

        given(doctorService.getDoctors()).willReturn(new ArrayList<>());
        given(mapperUtil.mapList(anyList(), eq(RecordDoctorViewModel.class))).willReturn(doctors);

        mockMvc.perform(MockMvcRequestBuilders.post("/records/update/{id}", id)
                        .with(csrf())
                        .flashAttr("record", recordViewModel))
                .andExpect(status().isOk())
                .andExpect(view().name("record-update"))
                .andExpect(model().attribute("doctors", doctors))
                .andDo(print());

        verifyNoInteractions(recordService);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void updateRecordTestEntityNotFoundException() throws Exception {
        Long id = 1L;

        RecordViewModel recordViewModel = getValidMockRecordViewModel();

        doThrow(new EntityNotFoundException("Error field doctorId"))
                .when(recordService).updateRecord(any(RecordDto.class), eq(id));

        List<RecordDoctorViewModel> doctors = List.of(
                RecordDoctorViewModel.builder().id(2L).firstName("John").lastName("Doe").build()
        );

        given(doctorService.getDoctors()).willReturn(new ArrayList<>());
        given(mapperUtil.mapList(anyList(), eq(RecordDoctorViewModel.class))).willReturn(doctors);

        mockMvc.perform(MockMvcRequestBuilders.post("/records/update/{id}", id)
                        .with(csrf())
                        .flashAttr("record", recordViewModel))
                .andExpect(status().isOk())
                .andExpect(view().name("record-update"))
                .andExpect(model().attribute("doctors", doctors))
                .andDo(print());

        verify(recordService, times(1)).updateRecord(any(RecordDto.class), eq(id));
    }


    private RecordViewModel getValidMockRecordViewModel() {
        return RecordViewModel.builder()
                .doctorId(2L)
                .doctorFirstName("Doctor")
                .doctorLastName("Doctorov")
                .patientFirstName("Pacient")
                .patientLastName("Pacientov")
                .patientEgn("0111111111")
                .diagnoseName("Diagnosis")
                .receipt("Receipt - paracetamol")
                .visitDate(LocalDate.parse("2025-07-20"))
                .build();
    }

}