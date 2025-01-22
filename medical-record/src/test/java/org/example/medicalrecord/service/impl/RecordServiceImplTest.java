package org.example.medicalrecord.service.impl;

import org.example.medicalrecord.data.dto.DiagnoseDto;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.dto.SickLeaveDto;
import org.example.medicalrecord.data.dto.UserDto;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.data.entity.*;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.exceptions.AuthorizationFailureException;
import org.example.medicalrecord.exceptions.EntityNotFoundException;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.repository.RecordRepository;
import org.example.medicalrecord.service.AuthenticationService;
import org.example.medicalrecord.service.DiagnoseService;
import org.example.medicalrecord.service.SickLeaveService;
import org.example.medicalrecord.util.ModelMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class RecordServiceImplTest {

    @MockitoBean
    private  RecordRepository recordRepository;

    @MockitoBean
    private  DoctorRepository doctorRepository;

    @MockitoBean
    private  PatientRepository patientRepository;

    @MockitoBean
    private  DiagnoseService diagnoseService;

    @MockitoBean
    private  SickLeaveService sickLeaveService;

    @MockitoBean
    private  ModelMapperUtil mapperUtil;

    @MockitoBean
    private  AuthenticationService authenticationService;

    @Autowired
    private RecordServiceImpl recordService;

    private Record record;

    private UserDto loggedInUser;

    ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        loggedInUser = UserDto.builder()
                .id(1L)
                .authorities(new HashSet<>())
                .build();
        given(authenticationService.getLoggedInUser()).willReturn(loggedInUser);
        mockModelMapper = mock(ModelMapper.class);
        given(mapperUtil.getModelMapper()).willReturn(mockModelMapper);
        record = Record.builder()
                .visitDate(java.sql.Date.valueOf("2024-10-10"))
                .build();
        record.setId(1L);
    }

    @Test
    void getRecordsWithoutUser() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> this.recordService.getRecords());
    }

    @Test
    @WithAnonymousUser
    void getRecordsWithAnonymousUser() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.getRecords());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void getRecordsWithStatusOk() {
        loggedInUser.setAuthorities(Set.of(Roles.ROLE_DOCTOR.name()));
        given(recordRepository.findAll()).willReturn(List.of(new Record(), new Record()));

        List<RecordDto> result = recordService.getRecords();

        assertThat(result).isNotNull();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_PATIENT"})
    void getPatientRecordsWithStatusOk() {
        loggedInUser.setAuthorities(new HashSet<>());
        given(recordRepository.findAll()).willReturn(new ArrayList<>());
        given(recordRepository.findAllByPatientId(loggedInUser.getId())).willReturn(List.of(new Record(), new Record()));

        List<RecordDto> result = recordService.getRecords();

        assertThat(result).isNotNull();
    }

    @Test
    void getRecordByIdWithoutUser() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> this.recordService.getRecord(1L));
    }

    @Test
    @WithAnonymousUser
    void getRecordByIdWithAnonymousUser() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.getRecord(1L));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void getRecordByIdWithStatusOk() {
        loggedInUser.setAuthorities(Set.of(Roles.ROLE_DOCTOR.name()));
        given(mapperUtil.getModelMapper()).willReturn(new ModelMapper());
        given(recordRepository.findById(1L)).willReturn(Optional.of(record));

        RecordDto result = recordService.getRecord(record.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(record.getId());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void getRecordByIdThrowsIfNotFound() {
        loggedInUser.setAuthorities(Set.of(Roles.ROLE_DOCTOR.name()));
        given(recordRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> recordService.getRecord(record.getId()));
    }

    @Test
    void filterRecordsWithoutUser() {
        Specification<Record> specification = mock(Specification.class);
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> this.recordService.filterRecords(specification));
    }

    @Test
    @WithAnonymousUser
    void filterRecordsWithAnonymousUser() {
        Specification<Record> specification = mock(Specification.class);
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.filterRecords(specification));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void filterRecordsWithStatusOk() {
        loggedInUser.setAuthorities(Set.of(Roles.ROLE_DOCTOR.name()));
        Specification<Record> specification = mock(Specification.class);
        given(mapperUtil.mapList(anyList(), eq(RecordDto.class))).willReturn(List.of(new RecordDto(), new RecordDto()));

        List<RecordDto> result = recordService.filterRecords(specification);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_PATIENT"})
    void filterRecordsAsPatientReturnsOnlyPatientRecords() {
        Specification<Record> mockSpec = mock(Specification.class);

        Record record1 = Record.builder().id(1L).patient(Patient.builder().id(1L).build()).build();
        Record record2 = Record.builder().id(2L).patient(Patient.builder().id(2L).build()).build();
        List<Record> allRecords = List.of(record1, record2);

        RecordDto recordDto1 = RecordDto.builder().id(1L).build();

        List<Record> filtered = List.of(record1);
        List<RecordDto> filteredDtos = List.of(recordDto1);

        given(recordRepository.findAll(any(Specification.class))).willReturn(allRecords);
        given(mapperUtil.mapList(filtered, RecordDto.class)).willReturn(filteredDtos);

        List<RecordDto> result = recordService.filterRecords(mockSpec);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(mapperUtil).mapList(filtered, RecordDto.class);
    }

    @Test
    void createRecordWithoutUser() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> this.recordService.createRecord(new RecordDto()));
    }

    @Test
    @WithAnonymousUser
    void createRecordWithAnonymousUser() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.createRecord(new RecordDto()));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_PATIENT"})
    void createRecordWithWrongUserAuthority() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.createRecord(new RecordDto()));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void createRecordWithStatusOk() {
        RecordDto recordDto = RecordDto.builder()
                .id(1L)
                .doctorId(2L)
                .patientEgn("1234567890")
                .diagnoseName("Flu")
                .receipt("Antibiotics")
                .startDate(java.sql.Date.valueOf("2024-01-01"))
                .leaveDays(5)
                .build();

        Record record = Record.builder().id(1L).build();
        Doctor doctor = Doctor.builder().id(2L).build();
        Patient patient = Patient.builder().id(3L).egn("1234567890").build();
        Diagnose diagnose = new Diagnose("Flu", "Antibiotics", record);
        SickLeave sickLeave = new SickLeave(recordDto.getStartDate(), recordDto.getLeaveDays(), record);

        given(mockModelMapper.map(recordDto, Record.class)).willReturn(record);
        given(doctorRepository.findById(recordDto.getDoctorId())).willReturn(Optional.of(doctor));
        given(patientRepository.findByEgnAndFirstNameAndLastName(recordDto.getPatientEgn(), null, null)).willReturn(Optional.of(patient));
        given(mockModelMapper.map(record, RecordDto.class)).willReturn(recordDto);
        given(recordRepository.save(any(Record.class))).willAnswer(invocation -> {
            Record saved = invocation.getArgument(0);
            saved.setDiagnose(diagnose);
            saved.setSickLeave(sickLeave);
            return saved;
        });

        RecordDto result = recordService.createRecord(recordDto);

        assertThat(result).isNotNull();
        assertThat(result.getDoctorId()).isEqualTo(2L);
        assertThat(result.getPatientEgn()).isEqualTo(patient.getEgn());
        assertThat(result.getStartDate()).isEqualTo(recordDto.getStartDate());
        assertThat(result.getLeaveDays()).isEqualTo(recordDto.getLeaveDays());
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void updateRecordWithoutUser() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> this.recordService.updateRecord(new RecordDto(), 1L));
    }

    @Test
    @WithAnonymousUser
    void updateRecordWithAnonymousUser() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.updateRecord(new RecordDto(), 1L));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_PATIENT"})
    void updateRecordWithWrongUserAuthority() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.updateRecord(new RecordDto(), 1L));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void updateRecordWithStatusOk() {
        RecordDto recordDto = RecordDto.builder()
                .id(1L)
                .visitDate(java.sql.Date.valueOf("2025-01-01"))
                .diagnoseName("Updated Diagnose")
                .startDate(java.sql.Date.valueOf("2025-02-01"))
                .leaveDays(2)
                .build();

        UserDto userDoctor = UserDto.builder()
                .id(2L)
                .authorities(Set.of("ROLE_DOCTOR"))
                .build();

        Patient patient = Patient.builder().id(3L).egn("1234567890").build();
        Doctor doctor = Doctor.builder().id(2L).build();
        Record existingRecord = Record.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .sickLeave(SickLeave.builder().startDate(java.sql.Date.valueOf("2025-01-01")).build())
                .build();

        given(recordRepository.findById(1L)).willReturn(Optional.of(existingRecord));
        given(authenticationService.getLoggedInUser()).willReturn(userDoctor);
        given(mockModelMapper.map(any(Record.class), eq(RecordDto.class))).willReturn(recordDto);
        given(doctorRepository.findById(recordDto.getDoctorId())).willReturn(Optional.of(mock(Doctor.class)));
        given(patientRepository.findByEgnAndFirstNameAndLastName(recordDto.getPatientEgn(), null, null)).willReturn(Optional.of(mock(Patient.class)));
        given(recordRepository.save(any(Record.class))).willAnswer(invocation -> invocation.getArgument(0));

        RecordDto result = recordService.updateRecord(recordDto, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getVisitDate()).isEqualTo(recordDto.getVisitDate());
        verify(diagnoseService, times(1)).updateDiagnose(any(DiagnoseDto.class), eq(1L));
        verify(sickLeaveService, times(1)).updateSickLeave(any(SickLeaveDto.class), eq(1L));
        verify(recordRepository).save(any(Record.class));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void updateRecordUnauthorized() {
        RecordDto recordDto = RecordDto.builder()
                .id(1L)
                .doctorId(2L)
                .patientEgn("333999")
                .build();

        UserDto userDoctor = UserDto.builder()
                .id(10L)
                .authorities(Set.of("ROLE_DOCTOR"))
                .build();

        Record existingRecord = Record.builder()
                .id(1L)
                .doctor(Doctor.builder().id(2L).build())
                .patient(Patient.builder().id(3L).egn("1234567890").build())
                .build();

        given(recordRepository.findById(1L)).willReturn(Optional.of(existingRecord));
        given(authenticationService.getLoggedInUser()).willReturn(userDoctor);

        assertThatThrownBy(() -> recordService.updateRecord(recordDto, 1L))
                .isInstanceOf(AuthorizationFailureException.class)
                .hasMessageContaining("You are not authorized to update this record");
        verifyNoMoreInteractions(diagnoseService);
        verifyNoMoreInteractions(sickLeaveService);
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void deleteRecordWithoutUser() {
        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> this.recordService.deleteRecord(1L));
    }

    @Test
    @WithAnonymousUser
    void deleteRecordWithAnonymousUser() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.deleteRecord(1L));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_PATIENT"})
    void deleteRecordWithWrongUserAuthority() {
        assertThrows(AccessDeniedException.class,
                () -> this.recordService.deleteRecord(1L));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void deleteRecordWithStatusOk() {
        recordService.deleteRecord(1L);

        verify(recordRepository, times(1)).deleteById(1L);
    }

}
