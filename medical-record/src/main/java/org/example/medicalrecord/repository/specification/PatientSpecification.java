package org.example.medicalrecord.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.util.DataUtil;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientSpecification {

    public static Specification<Patient> filterRecords(String firstName, String lastName, String egn, Long gpId,
                                                       Date lastPaidFrom, Date lastPaidTo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (DataUtil.isNotEmpty(firstName)) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
            }
            if (DataUtil.isNotEmpty(lastName)) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName+ "%"));
            }
            if (DataUtil.isNotEmpty(egn)) {
                predicates.add(criteriaBuilder.equal(root.get("egn"), egn));
            }
            if (gpId != null) {
                predicates.add(criteriaBuilder.equal(root.get("gp").get("id"), gpId));
            }
            if (lastPaidFrom != null) {
                if (lastPaidTo != null) {
                    predicates.add(criteriaBuilder.between(root.get("lastPaidMedicalInsurance"), lastPaidFrom, lastPaidTo));
                } else {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("lastPaidMedicalInsurance"), lastPaidFrom));
                }
            }
            if (lastPaidTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("lastPaidMedicalInsurance"), lastPaidTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
