package ru.slisarenko.spring_data_projections.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.slisarenko.spring_data_projections.model.entity.EmployeeEntity;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeDTO;
import ru.slisarenko.spring_data_projections.service.dto.EmployeeRequestDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface EmployeeMapper {
    public abstract EmployeeDTO toDTO(EmployeeEntity entity);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract EmployeeEntity toEntity(EmployeeRequestDTO dto);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract EmployeeEntity updateEntity(@MappingTarget EmployeeEntity fromDB, EmployeeRequestDTO entityIn);
}
