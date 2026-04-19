package ru.slisarenko.spring_data_jdbc.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.slisarenko.spring_data_jdbc.dto.BookDTO;
import ru.slisarenko.spring_data_jdbc.entityes.BookEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface MapperEntityToDTO {
    public abstract BookDTO toDTO(BookEntity book);
    public abstract BookEntity toEntity(BookDTO book);
}
