package com.example.prog4.repository.entity.cnaps;

import com.example.prog4.repository.entity.employee.enums.Csp;
import com.example.prog4.repository.entity.employee.enums.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "\"employee_cnaps\"")
public class Employee {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    private String cin;
    private String cnaps;
    private String image;
    private String address;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "personal_email")
    private String personalEmail;
    @Column(name = "professional_email")
    private String professionalEmail;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "entrance_date")
    private LocalDate entranceDate;
    @Column(name = "departure_date")
    private LocalDate departureDate;
    @Column(name = "children_number")
    private Integer childrenNumber;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(sex AS varchar)", write = "CAST(? AS sex)")
    private Sex sex;
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(csp AS varchar)", write = "CAST(? AS csp)")
    private Csp csp;
    @Column(name = "end_to_end_id")
    private String endToEndId;
}
