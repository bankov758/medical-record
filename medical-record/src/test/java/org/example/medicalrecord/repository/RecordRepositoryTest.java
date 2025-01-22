package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.Doctor;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
public class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void findAllByPatientIdTest() {
        Patient patient = Patient.builder()
                .firstName("Imen")
                .lastName("Imenov")
                .username("imm22")
                .password("secret123")
                .egn("1111111111")
                .build();
        Patient patient2 = Patient.builder()
                .firstName("Imenka")
                .lastName("Imenkova")
                .username("imm33")
                .password("secret123")
                .egn("2222222222")
                .build();
        Doctor doctor = Doctor.builder()
                .firstName("Imen")
                .lastName("Imenov")
                .username("imm22")
                .password("secret123")
                .build();
        doctorRepository.save(doctor);
        patientRepository.saveAll(List.of(patient, patient2));

        Record recordA = Record.builder().patient(patient).doctor(doctor).visitDate(java.sql.Date.valueOf("2025-02-01")).build();
        Record recordB = Record.builder().patient(patient).doctor(doctor).visitDate(java.sql.Date.valueOf("2025-02-01")).build();
        Record recordC = Record.builder().patient(patient2).doctor(doctor).visitDate(java.sql.Date.valueOf("2025-02-01")).build();

        recordRepository.saveAll(List.of(recordA, recordB, recordC));

        List<Record> recordsForPatient = recordRepository.findAllByPatientId(patient.getId());
        List<Record> recordsForPatient2 = recordRepository.findAllByPatientId(patient2.getId());

        assertThat(recordsForPatient)
                .hasSize(2)
                .containsExactlyInAnyOrder(recordA, recordB);

        assertThat(recordsForPatient2)
                .hasSize(1)
                .containsExactly(recordC);

    }

}
