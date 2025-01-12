package org.example.medicalrecord.repository;

import jakarta.persistence.criteria.Predicate;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.util.DataUtil;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecordSpecification {

    public static Specification<Record> filterRecords(String doctorFirstName, String doctorLastName, String patientFirstName,
                                                      String patientLastName, String patientEgn, String diagnoseName, Date visitDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (DataUtil.isNotEmpty(doctorFirstName)) {
                predicates.add(criteriaBuilder.equal(root.get("doctor").get("firstName"), doctorFirstName));
            }
            if (DataUtil.isNotEmpty(doctorLastName)) {
                predicates.add(criteriaBuilder.equal(root.get("doctor").get("lastName"), doctorLastName));
            }
            if (DataUtil.isNotEmpty(patientFirstName)) {
                predicates.add(criteriaBuilder.equal(root.get("patient").get("firstName"), patientFirstName));
            }
            if (DataUtil.isNotEmpty(patientLastName)) {
                predicates.add(criteriaBuilder.equal(root.get("patient").get("lastName"), patientLastName));
            }
            if (DataUtil.isNotEmpty(patientEgn)) {
                predicates.add(criteriaBuilder.equal(root.get("patient").get("egn"), patientEgn));
            }
            if (DataUtil.isNotEmpty(diagnoseName)) {
                predicates.add(criteriaBuilder.equal(root.get("diagnose").get("diagnoseName"), diagnoseName));
            }
            if (visitDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("visitDate"), visitDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


