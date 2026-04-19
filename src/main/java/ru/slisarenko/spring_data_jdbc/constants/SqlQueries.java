package ru.slisarenko.spring_data_jdbc.constants;

public class SqlQueries {
    public static final String INSERT_SQL = "INSERT INTO book (title, author, publication_year) VALUES (?, ?, ?)";
    public static final String UPDATE_SQL = "UPDATE book SET title = ?, author = ?, publication_year = ? WHERE id = ?";
    public static final String SELECT_BY_ID_SQL = "SELECT * FROM book WHERE id = ?";
    public static final String SELECT_ALL_SQL = "SELECT * FROM book";
    public static final String SELECT_COUNT_SQL = "SELECT COUNT(*) FROM book WHERE id = ?";
    public static final String DELETE_BY_ID_SQL = "DELETE FROM book WHERE id = ?";
}
