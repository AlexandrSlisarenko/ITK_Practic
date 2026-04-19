package ru.slisarenko.spring_data_jdbc.repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.slisarenko.spring_data_jdbc.entityes.BookEntity;

import static ru.slisarenko.spring_data_jdbc.constants.SqlQueries.DELETE_BY_ID_SQL;
import static ru.slisarenko.spring_data_jdbc.constants.SqlQueries.INSERT_SQL;
import static ru.slisarenko.spring_data_jdbc.constants.SqlQueries.SELECT_ALL_SQL;
import static ru.slisarenko.spring_data_jdbc.constants.SqlQueries.SELECT_BY_ID_SQL;
import static ru.slisarenko.spring_data_jdbc.constants.SqlQueries.SELECT_COUNT_SQL;
import static ru.slisarenko.spring_data_jdbc.constants.SqlQueries.UPDATE_SQL;
import static ru.slisarenko.spring_data_jdbc.mapper.BookMapper.BOOK_ROW_MAPPER;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookEntity save(BookEntity book) {
        if (book.getId() == null) {
            return insert(book);
        } else {
            return update(book);
        }
    }

    private BookEntity insert(BookEntity book) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setObject(3, book.getPublicationYear());
            return ps;
        }, keyHolder);
        book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return book;
    }

    private BookEntity update(BookEntity book) {
        jdbcTemplate.update(UPDATE_SQL, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
        return book;
    }

    public Optional<BookEntity> findById(Long id) {
        try {
            var book = jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, BOOK_ROW_MAPPER, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<BookEntity> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, BOOK_ROW_MAPPER);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    public boolean existsById(Long id) {
        var count = jdbcTemplate.queryForObject(SELECT_COUNT_SQL, Integer.class, id);
        return count != null && count > 0;
    }
}
