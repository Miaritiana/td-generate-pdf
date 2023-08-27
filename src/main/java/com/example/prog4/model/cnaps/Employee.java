package com.example.prog4.model.cnaps;

import com.example.prog4.repository.entity.employee.enums.Csp;
import com.example.prog4.repository.entity.employee.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
public class Employee {
    private String id;
    private String firstName;
    private String lastName;

    private MultipartFile image;
    private String stringImage;

    private Csp csp;
    private Sex sex;
    private String cin;
    private String cnaps;
    private String address;
    private Integer childrenNumber;
    private String personalEmail;
    private String professionalEmail;
    private String registrationNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate entranceDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate departureDate;
    private String end_to_end_id;
}
