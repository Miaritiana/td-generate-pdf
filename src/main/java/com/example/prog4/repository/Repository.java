package com.example.prog4.repository;

import com.example.prog4.model.utilities.DateRange;
import com.example.prog4.repository.entity.employee.Employee;
import com.example.prog4.repository.entity.employee.enums.Sex;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Repository {

    List<Employee> findAll(
            String lastName,
            String firstName,
            String countryCode,
            Sex sex,
            String position,
            DateRange entranceRange,
            DateRange departureRange,
            Pageable pageable
    );

    Optional<Employee> findById(String id);
    void save(Employee employee);
}
