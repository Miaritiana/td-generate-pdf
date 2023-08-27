package com.example.prog4.repository.entity.employee;

import com.example.prog4.repository.entity.employee.enums.Csp;
import com.example.prog4.repository.entity.employee.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "\"employee\"")
public class Employee implements Serializable {
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
    @Column(name = "registration_number")
    private String registrationNumber;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "entrance_date")
    private LocalDate entranceDate;
    @Column(name = "departure_date")
    private LocalDate departureDate;
    @Column(name = "children_number")
    private Integer childrenNumber;
    @Column(name = "salary")
    private double salary;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(sex AS varchar)", write = "CAST(? AS sex)")
    private Sex sex;
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(csp AS varchar)", write = "CAST(? AS csp)")
    private Csp csp;

    @ManyToMany
    @JoinTable(
            name = "have_position",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    @ToString.Exclude
    private List<Position> positions;
    @OneToMany
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ToString.Exclude
    private List<Phone> phones;
}