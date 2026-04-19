package ru.slisarenko.spring_data_jdbc.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.slisarenko.spring_data_jdbc.entityes.BookEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@JdbcTest
@Import(BookRepository.class)
@Sql(scripts = "/book-schema.sql")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void save_createsBook() {
        var book = BookEntity.builder()
                .publicationYear(2026)
                .title("Test Title")
                .author("Test Author")
                .build();
        var saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Title");
        assertThat(saved.getAuthor()).isEqualTo("Test Author");
        assertThat(saved.getPublicationYear()).isEqualTo(2026);
    }

    @Test
    void save_updatesBook() {
        var book = BookEntity.builder()
                .publicationYear(2026)
                .title("Test Title")
                .author("Test Author")
                .build();
        var saved = bookRepository.save(book);
        saved.setTitle("Updated Title");
        saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Updated Title");
        assertThat(saved.getAuthor()).isEqualTo("Test Author");
        assertThat(saved.getPublicationYear()).isEqualTo(2026);
    }



    @Test
    void findById() {
        var book = BookEntity.builder()
                .publicationYear(2026)
                .title("Test Title")
                .author("Test Author")
                .build();
        var saved = bookRepository.save(book);
        var bookFromDB = bookRepository.findById(saved.getId()).orElse(null);

        Assertions.assertNotNull(bookFromDB);
        assertThat(bookFromDB.getId()).isNotNull();
        assertThat(bookFromDB.getTitle()).isEqualTo("Test Title");
        assertThat(bookFromDB.getAuthor()).isEqualTo("Test Author");
        assertThat(bookFromDB.getPublicationYear()).isEqualTo(2026);
    }

    @Test
    void deleteById() {
        var book = BookEntity.builder()
                .publicationYear(2026)
                .title("Test Title")
                .author("Test Author")
                .build();
        var saved = bookRepository.save(book);
        bookRepository.deleteById(saved.getId());
        var bookFromDB = bookRepository.findById(saved.getId()).orElse(null);

        Assertions.assertNull(bookFromDB);

    }

    @Test
    void findAll() {
        var book = BookEntity.builder()
                .publicationYear(2026)
                .title("Test Title")
                .author("Test Author")
                .build();
        var saved1 = bookRepository.save(book);
        var bookFromDB = bookRepository.findAll();

        Assertions.assertNotNull(bookFromDB);
        Assertions.assertEquals(1, bookFromDB.size());


    }

}