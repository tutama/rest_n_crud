package com.example.rest_n_crud;

import org.springframework.stereotype.Component;

@Component
public class StudentServiceImpl implements StudentService {

    @Override
    public Student save(Student student) {
        return student;
    }
}

