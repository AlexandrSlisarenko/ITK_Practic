package ru.slisarenko.spring_data_jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.slisarenko.spring_data_jdbc.entityes.BookEntity;

public class MapperResultSetToEntity {

    public static final RowMapper<BookEntity> BOOK_ROW_MAPPER = (rs, rowNum) -> {
        BookEntity book = new BookEntity();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublicationYear(rs.getInt("publication_year"));
        return book;
    };
}
