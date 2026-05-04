package ru.slisarenko.spring_data_projections.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.spring_data_projections.service.AccountingOfEmployeesService;
import ru.slisarenko.spring_data_projections.service.dto.DepartmentDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final AccountingOfEmployeesService accountingOfEmployeesService;

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO department) {
        DepartmentDTO departmentFromDB = accountingOfEmployeesService.createDepartment(department);
        return new ResponseEntity<>(departmentFromDB, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Long id) {
        return new ResponseEntity<>(accountingOfEmployeesService.getDepartmentById(id), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return new ResponseEntity<>(accountingOfEmployeesService.getAllDepartments(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO department) {
        return new ResponseEntity<>(accountingOfEmployeesService.updateDepartment(id,department), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        accountingOfEmployeesService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
