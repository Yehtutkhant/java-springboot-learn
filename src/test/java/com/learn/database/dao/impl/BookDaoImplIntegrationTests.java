package com.learn.database.dao.impl;

import com.learn.database.TestDataUtils;
import com.learn.database.domain.Author;
import com.learn.database.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookDaoImplIntegrationTests {

    private final BookDaoImpl bookDaoTest;
    private final AuthorDaoImpl authorDaoTest;


    @Autowired
    public BookDaoImplIntegrationTests(BookDaoImpl bookDao, AuthorDaoImpl authorDao) {
        this.bookDaoTest = bookDao;
        this.authorDaoTest = authorDao;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(author);

        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author.getId());
        bookDaoTest.create(book);

        Optional<Book> results =  bookDaoTest.findOne(book.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(book);
    }

    @Test
    public void testThatManyBooksCanBeCreatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(author);

        Book book1 = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author.getId());
        bookDaoTest.create(book1);

        Book book2 = TestDataUtils.createTestBook("978-1-2345-6789-1", "Beyond Horizon", author.getId());
        bookDaoTest.create(book2);

        Book book3 = TestDataUtils.createTestBook("978-1-2345-6789-2", "The Last Ember", author.getId());
        bookDaoTest.create(book3);

        List<Book> results = bookDaoTest.find();
        assertThat(results).hasSize(3).containsExactly(book1, book2, book3);
    }

    @Test
    public void testThatBookCanBeUpdatedAndRecalled() {
        Author author = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(author);

        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author.getId());
        bookDaoTest.create(book);

        book.setTitle("Updated Title");
        bookDaoTest.update(book.getIsbn(), book);

        Optional<Book> results = bookDaoTest.findOne(book.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(book);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        Author author = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(author);

        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author.getId());
        bookDaoTest.create(book);

        bookDaoTest.delete(book.getIsbn());

        Optional<Book> result = bookDaoTest.findOne(book.getIsbn());
        assertThat(result).isEmpty();
    }
}
