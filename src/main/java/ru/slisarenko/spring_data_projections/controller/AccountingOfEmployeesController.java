package ru.slisarenko.spring_data_projections.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.spring_data_projections.service.AccountingOfEmployeesService;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeDTO;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeDataDTO;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeRequestDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class AccountingOfEmployeesController {

    private final AccountingOfEmployeesService accountingOfEmployeesService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeRequestDTO employee) {
        var employeeFromDB = accountingOfEmployeesService.createEmployee(employee);
        return new ResponseEntity<>(employeeFromDB, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDataDTO> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(accountingOfEmployeesService.getEmployee(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeDataDTO>> getAllEmployees(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(accountingOfEmployeesService.getAllEmployees(page,size), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO employee) {
        return new ResponseEntity<>(accountingOfEmployeesService.updateEmployee(id,employee), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        accountingOfEmployeesService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
