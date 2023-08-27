package com.example.prog4.controller.mapper;

import com.example.prog4.model.employee.Employee;
import com.example.prog4.model.exception.BadRequestException;
import com.example.prog4.model.exception.NotFoundException;
import com.example.prog4.repository.cnaps.CnapsRepository;
import com.example.prog4.repository.employee.EmployeeRepository;
import com.example.prog4.repository.employee.PositionRepository;
import com.example.prog4.repository.entity.employee.Phone;
import com.example.prog4.repository.entity.employee.Position;
import com.example.prog4.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
public class EmployeeMapper {
    private PositionRepository positionRepository;
    private PhoneMapper phoneMapper;
    private EmployeeRepository employeeRepository;
    private CnapsRepository cnapsRepository;

    public com.example.prog4.repository.entity.employee.Employee toDomain(Employee employee) {
        try {
            List<Position> positions = new ArrayList<>();
            employee.getPositions().forEach(position -> {
                Optional<Position> position1 = positionRepository.findPositionByNameEquals(position.getName());
                if (position1.isEmpty()) {
                    positions.add(positionRepository.save(position));
                } else {
                    positions.add(position1.get());
                }
            });

            List<Phone> phones = employee.getPhones().stream().map((com.example.prog4.model.employee.Phone fromView) -> phoneMapper.toDomain(fromView, employee.getId())).toList();

            com.example.prog4.repository.entity.employee.Employee domainEmployee = com.example.prog4.repository.entity.employee.Employee.builder()
                    .id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .address(employee.getAddress())
                    .cin(employee.getCin())
                    .cnaps(employee.getCnaps())
                    .registrationNumber(employee.getRegistrationNumber())
                    .childrenNumber(employee.getChildrenNumber())
                    .salary(employee.getSalary())
                    // enums
                    .csp(employee.getCsp())
                    .sex(employee.getSex())
                    // emails
                    .professionalEmail(employee.getProfessionalEmail())
                    .personalEmail(employee.getPersonalEmail())
                    // dates
                    .birthDate(employee.getBirthDate())
                    .departureDate(employee.getDepartureDate())
                    .entranceDate(employee.getEntranceDate())
                    // lists
                    .phones(phones)
                    .positions(positions)
                    .build();
            MultipartFile imageFile = employee.getImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                domainEmployee.setImage("data:image/jpeg;base64," + base64Image);
            }
            return domainEmployee;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public Employee toView(com.example.prog4.repository.entity.employee.Employee employee) {
        return Employee.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .address(employee.getAddress())
                .cin(employee.getCin())
                .cnaps(employee.getCnaps())
                .registrationNumber(employee.getRegistrationNumber())
                .childrenNumber(employee.getChildrenNumber())
                .salary(employee.getSalary())
                // enums
                .csp(employee.getCsp())
                .sex(employee.getSex())
                .stringImage(employee.getImage())
                // emails
                .professionalEmail(employee.getProfessionalEmail())
                .personalEmail(employee.getPersonalEmail())
                // dates
                .birthDate(employee.getBirthDate())
                .departureDate(employee.getDepartureDate())
                .entranceDate(employee.getEntranceDate())
                // lists
                .phones(employee.getPhones().stream().map(phoneMapper::toView).toList())
                .positions(employee.getPositions())
                .build();
    }

    public com.example.prog4.repository.entity.cnaps.Employee toCnapsEmployeeView(com.example.prog4.repository.entity.employee.Employee employee) {
        return com.example.prog4.repository.entity.cnaps.Employee.builder()
                .id(employee.getId())
                .cin(employee.getCin())
                .cnaps(cnapsRepository.findByEndToEndId(employee.getId()).orElseThrow(
                        () -> new NotFoundException("employee not found")
                ).getCnaps())
                .image(employee.getImage())
                .address(employee.getAddress())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .personalEmail(employee.getPersonalEmail())
                .professionalEmail(employee.getProfessionalEmail())
                .birthDate(employee.getBirthDate())
                .entranceDate(employee.getEntranceDate())
                .departureDate(employee.getDepartureDate())
                .childrenNumber(employee.getChildrenNumber())
                .sex(employee.getSex())
                .csp(employee.getCsp())
                .endToEndId(employee.getId())
                .build();
    }

    public com.example.prog4.repository.entity.cnaps.Employee toCnapsEmployeeCreate(com.example.prog4.repository.entity.employee.Employee employee) {
        return com.example.prog4.repository.entity.cnaps.Employee.builder()
                .id(employee.getId())
                .cin(employee.getCin())
                .image(employee.getImage())
                .address(employee.getAddress())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .personalEmail(employee.getPersonalEmail())
                .professionalEmail(employee.getProfessionalEmail())
                .birthDate(employee.getBirthDate())
                .entranceDate(employee.getEntranceDate())
                .departureDate(employee.getDepartureDate())
                .childrenNumber(employee.getChildrenNumber())
                .sex(employee.getSex())
                .csp(employee.getCsp())
                .endToEndId(employee.getId())
                .build();
    }

    public com.example.prog4.repository.entity.employee.Employee toEmployee(com.example.prog4.repository.entity.cnaps.Employee cnapsEmployee) {
        return com.example.prog4.repository.entity.employee.Employee.builder()
                .id(cnapsEmployee.getId())
                .cin(cnapsEmployee.getCin())
                .cnaps(cnapsEmployee.getCnaps())
                .image(cnapsEmployee.getImage())
                .address(cnapsEmployee.getAddress())
                .lastName(cnapsEmployee.getLastName())
                .firstName(cnapsEmployee.getFirstName())
                .personalEmail(cnapsEmployee.getPersonalEmail())
                .professionalEmail(cnapsEmployee.getProfessionalEmail())
                .registrationNumber(employeeRepository.findById(cnapsEmployee.getId()).orElseThrow(
                        () -> new NotFoundException("employee not found")
                ).getRegistrationNumber())
                .salary(employeeRepository.findById(cnapsEmployee.getId()).orElseThrow(
                        () -> new NotFoundException("employee not found")
                ).getSalary())
                .birthDate(cnapsEmployee.getBirthDate())
                .entranceDate(cnapsEmployee.getEntranceDate())
                .departureDate(cnapsEmployee.getDepartureDate())
                .childrenNumber(cnapsEmployee.getChildrenNumber())
                .sex(cnapsEmployee.getSex())
                .csp(cnapsEmployee.getCsp())
                .positions(employeeRepository.findById(cnapsEmployee.getId()).orElseThrow(
                        () -> new NotFoundException("employee not found")
                ).getPositions())
                .phones(employeeRepository.findById(cnapsEmployee.getId()).orElseThrow(
                        () -> new NotFoundException("employee not found")
                ).getPhones())
                .build();
    }
}
