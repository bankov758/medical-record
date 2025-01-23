package org.example.medicalrecord.integration;

import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.repository.DoctorRepository;
import org.example.medicalrecord.repository.PatientRepository;
import org.example.medicalrecord.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RecordRepository recordRepository;

    @BeforeEach
    void setup() {
        recordRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        Doctor doctor = Doctor.builder()
                .firstName("John")
                .lastName("Doe")
                .username("doctor")
                .password("password")
                .build();

        Patient patient = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("jane")
                .password("password")
                .egn("1234567890")
                .build();

        doctor = doctorRepository.save(doctor);
        patient = patientRepository.save(patient);

        Record record = Record.builder()
                .doctor(doctor)
                .patient(patient)
                .visitDate(java.sql.Date.valueOf("2025-02-01"))
                .build();
        recordRepository.save(record);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_DOCTOR"})
    void getRecords_IntegrationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/records"))
                .andExpect(status().isOk())
                .andExpect(view().name("records"))
                .andExpect(model().attributeExists("records"))
                .andExpect(model().attributeExists("searchRecord"))
                .andDo(print());
    }
}

