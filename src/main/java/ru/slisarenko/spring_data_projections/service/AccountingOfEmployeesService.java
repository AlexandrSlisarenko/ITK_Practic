package ru.slisarenko.spring_data_projections.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_data_projections.model.entity.DepartmentEntity;
import ru.slisarenko.spring_data_projections.model.repository.DepartmentRepository;
import ru.slisarenko.spring_data_projections.model.repository.EmployeeRepository;
import ru.slisarenko.spring_data_projections.service.dto.DepartmentDTO;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeDTO;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeDataDTO;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeRequestDTO;
import ru.slisarenko.spring_data_projections.service.exception.DepartmentNotFoundException;
import ru.slisarenko.spring_data_projections.service.exception.EmployeeNotFoundException;
import ru.slisarenko.spring_data_projections.service.mapper.DepartmentMapper;
import ru.slisarenko.spring_data_projections.service.mapper.EmployeeMapper;
import ru.slisarenko.spring_data_projections.service.mapper.EmployeeProjectionMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountingOfEmployeesService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentMapper departmentMapper;
    private final EmployeeProjectionMapper mapper;

    public EmployeeDTO createEmployee(EmployeeRequestDTO employee) {
        var employeeEntity = this.employeeMapper.toEntity(employee);
        var departmentFromDB = getDepartmentFromDB(employee.departmentId());
        employeeEntity.setDepartment(departmentFromDB);
        employeeEntity = employeeRepository.save(employeeEntity);
        return this.employeeMapper.toDTO(employeeEntity);
    }

    public Page<EmployeeDataDTO> getAllEmployees(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var employees = this.employeeRepository.findAllProjection();
        var content = employees.stream().map(mapper::toEmployeeData).toList();
        return new PageImpl<>(content, pageable, content.size());
    }


    public EmployeeDataDTO getEmployee(Long id) {
        var employee = this.employeeRepository.findProjectionById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return this.mapper.toEmployeeData(employee);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeRequestDTO employeeDetails) {
        var employeeFromDB = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        if(employeeFromDB.getDepartment().getId() != employeeDetails.departmentId()) {
            var departmentFromDB = getDepartmentFromDB(employeeDetails.departmentId());
            employeeFromDB.setDepartment(departmentFromDB);
        }
        employeeFromDB = this.employeeMapper.updateEntity(employeeFromDB,employeeDetails);
        employeeFromDB = this.employeeRepository.save(employeeFromDB);
        return this.employeeMapper.toDTO(employeeFromDB);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public DepartmentDTO createDepartment(DepartmentDTO department) {
        var departmentEntity = this.departmentMapper.toEntity(department);
        departmentEntity = departmentRepository.save(departmentEntity);
        return this.departmentMapper.toDTO(departmentEntity);
    }

    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDTO)
                .orElseGet(() -> new DepartmentDTO(-1L,"Not Found"));
    }

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream().map(departmentMapper::toDTO).toList();
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDetails) {
        var departmentFromDB = getDepartmentFromDB(id);
        departmentFromDB.setName(departmentDetails.name());
        departmentFromDB = departmentRepository.save(departmentFromDB);
        return this.departmentMapper.toDTO(departmentFromDB);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentEntity getDepartmentFromDB(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
    }
}
