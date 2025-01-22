package org.example.medicalrecord.repository;

import org.example.medicalrecord.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

@DataJpaTest()
@ActiveProfiles("test")
public class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllByPatientId_ReturnsMatchingRecords() {
        User user = new User("ivo", "bankov", "bankov758", "123123123", new HashSet<>());
        userRepository.save(user);

    }

}
