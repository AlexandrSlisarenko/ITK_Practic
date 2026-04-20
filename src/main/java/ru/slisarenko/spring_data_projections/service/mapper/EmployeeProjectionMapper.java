package ru.slisarenko.spring_data_projections.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.spring_data_projections.model.repository.EmployeeProjection;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeDataDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface EmployeeProjectionMapper {
    @Mapping(target = "data", expression = "java(employee.getFullName() + \" \" + employee.getPosition() + \"  \" + employee.getDepartmentName())")
    public abstract EmployeeDataDTO toEmployeeData(EmployeeProjection employee);
}
