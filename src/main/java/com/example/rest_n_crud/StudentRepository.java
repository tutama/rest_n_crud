package com.example.rest_n_crud;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByFirstName(String firstName);
    List<Student> findByLastName(String lastName);
}