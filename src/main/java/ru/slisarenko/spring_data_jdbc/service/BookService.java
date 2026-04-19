package ru.slisarenko.spring_data_jdbc.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_data_jdbc.dto.BookDTO;
import ru.slisarenko.spring_data_jdbc.entityes.BookEntity;
import ru.slisarenko.spring_data_jdbc.exceptions.BookNotfoundException;
import ru.slisarenko.spring_data_jdbc.mapper.MapperEntityToDTO;
import ru.slisarenko.spring_data_jdbc.repository.BookRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MapperEntityToDTO mapper;

    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        return this.bookRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDTO findById(Long id) {
        var book = this.bookRepository.findById(id)
                .orElseThrow(() -> new BookNotfoundException(id));
        return mapper.toDTO(book);
    }

    public BookDTO create(BookDTO book) {
        var bookEntity = this.mapper.toEntity(book);
        var bookFromDB = this.bookRepository.save(bookEntity);
        return mapper.toDTO(bookFromDB);
    }

    public BookDTO update(Long id, BookDTO book) {
        var bookEntity = this.mapper.toEntity(book);
        bookEntity.setId(id);
        var bookFromDB = this.bookRepository.save(bookEntity);
        return mapper.toDTO(bookFromDB);
    }

    public void delete(Long bookId) {
        this.bookRepository.deleteById(bookId);
    }


}
