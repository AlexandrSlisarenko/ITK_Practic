package ru.slisarenko.spring_data_projections.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.slisarenko.spring_data_projections.model.entity.DepartmentEntity;
import ru.slisarenko.spring_data_projections.service.dto.DepartmentDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface DepartmentMapper {
    public abstract DepartmentDTO toDTO(DepartmentEntity entity);
    public abstract DepartmentEntity toEntity(DepartmentDTO dto);
}
