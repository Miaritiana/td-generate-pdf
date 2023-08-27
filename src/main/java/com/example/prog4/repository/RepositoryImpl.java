package com.example.prog4.repository;

import com.example.prog4.controller.mapper.EmployeeMapper;
import com.example.prog4.model.utilities.DateRange;
import com.example.prog4.repository.cnaps.CnapsRepository;
import com.example.prog4.repository.dao.EmployeeManagerDao;
import com.example.prog4.repository.employee.EmployeeRepository;
import com.example.prog4.repository.entity.employee.Employee;
import com.example.prog4.repository.entity.employee.enums.Sex;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
@AllArgsConstructor
public class RepositoryImpl implements Repository{

    private final CnapsRepository cnapsRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeManagerDao employeeManagerDao;
    private final EmployeeMapper mapper;

    @Override
    public List<Employee> findAll(
            String lastName,
            String firstName,
            String countryCode,
            Sex sex,
            String position,
            DateRange entranceRange,
            DateRange departureRange,
            Pageable pageable
    ) {
        List<Employee> employees = employeeManagerDao.findByCriteria(
                lastName, firstName, countryCode, sex, position, entranceRange, departureRange, pageable
        );
        List<com.example.prog4.repository.entity.cnaps.Employee> employeeList = employees.stream()
                .map(mapper::toCnapsEmployeeView)
                .collect(Collectors.toUnmodifiableList());
        return employeeList.stream()
                .map(mapper::toEmployee)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Employee> findById(String id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        assert employee != null;
        com.example.prog4.repository.entity.cnaps.Employee employeeCnaps = mapper.toCnapsEmployeeView(employee);
        Employee result = mapper.toEmployee(employeeCnaps);
        return Optional.of(result);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
        cnapsRepository.save(mapper.toCnapsEmployeeCreate(employee));
    }
}
