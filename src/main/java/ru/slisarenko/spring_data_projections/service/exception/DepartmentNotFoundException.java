package ru.slisarenko.spring_data_projections.service.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {

        super(String.format("Department with id = %s not found!", id.toString()));
    }
}
