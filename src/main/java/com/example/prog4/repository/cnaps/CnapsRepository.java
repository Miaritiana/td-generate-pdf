package com.example.prog4.repository.cnaps;

import com.example.prog4.repository.entity.cnaps.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CnapsRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByCin(String cin);

    Optional<Employee> findByEndToEndId(String end_to_end_id);



}
