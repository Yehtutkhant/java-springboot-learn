package com.learn.database.dao.impl;


import com.learn.database.TestDataUtils;
import com.learn.database.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookDaoImpl underTest;

    @Test
    public void testThatCreateGeneratesCorrectSql() {

        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", 1L);
        underTest.create(book);

        verify(jdbcTemplate).update(
                eq("INSERT INTO books (isbn, title, author_id) VALUES (?, ?, ?)"),
                eq("978-1-2345-6789-0"),
                eq("The Shadow in the Attic"),
                eq(1L)
        );
    }


    @Test
    public void testThatFindOneGeneratesCorrectSql() {

        underTest.findOne("978-1-2345-6789-0");

        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id from books WHERE isbn = ? LIMIT 1"),
                ArgumentMatchers.<BookDaoImpl.BookRowMapper>any(),
                eq("978-1-2345-6789-0")
        );
    }

    @Test
    public void testThatFindManyGeneratesCorrectSql() {

        underTest.find();

        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id from books"),
                ArgumentMatchers.<BookDaoImpl.BookRowMapper>any()
        );
    }

    @Test
    public void testThatUpdateGeneratesCorrectSql() {
        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", 1L);
        underTest.update("978-1-2345-6789-1", book);

        verify(jdbcTemplate).update(
                eq("UPDATE books SET isbn = ?, title = ?, author_id = ? WHERE isbn = ?"),
                eq("978-1-2345-6789-0"), eq("The Shadow in the Attic"), eq(1L), eq("978-1-2345-6789-1")
        );
    }

    @Test
    public void testThatDeleteGeneratesCorrectSql() {
        underTest.delete("978-1-2345-6789-0");

        verify(jdbcTemplate).update(
                eq("DELETE FROM books WHERE isbn = ?"), eq("978-1-2345-6789-0")
        );
    }



}
