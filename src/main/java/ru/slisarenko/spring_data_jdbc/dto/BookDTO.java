package ru.slisarenko.spring_data_jdbc.dto;

import lombok.Builder;

@Builder
public record BookDTO(Long id,
        String title,
        String author,
        Integer publicationYear) {
}
