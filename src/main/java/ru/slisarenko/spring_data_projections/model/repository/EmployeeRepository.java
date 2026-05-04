package ru.slisarenko.spring_data_projections.model.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.slisarenko.spring_data_projections.model.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Query(value = "SELECT CONCAT(employee.firstName, ' ', employee.lastName) AS fullNmae, " +
                   "employee.position AS position, " +
                   "department.name AS departmentName " +
                   "FROM EmployeeEntity employee JOIN DepartmentEntity department " +
                   "ON employee.department.id = department.id " +
                   "WHERE employee.id = :id")
    Optional<EmployeeProjection> findProjectionById(Long id);

    @Query(value = "SELECT CONCAT(employee.firstName, ' ', employee.lastName) AS fullNmae, " +
                   "employee.position AS position, " +
                   "department.name AS departmentName " +
                   "FROM EmployeeEntity employee JOIN DepartmentEntity department " +
                   "ON employee.department.id = department.id ")
    List<EmployeeProjection> findAllProjection();
}
