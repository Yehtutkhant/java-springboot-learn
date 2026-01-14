package com.learn.database.repositories;

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
public class BookRepositoryIntegrationTests {

    private final BookRepository bookRepositoryTest;

    // have to use this bean to save author to the db explicitly because saving author from cascading from the book has the id of null for authors
    private final AuthorRepository authorRepositoryTest;


    @Autowired
    public BookRepositoryIntegrationTests(BookRepository bookRepositoryTest, AuthorRepository authorRepositoryTest) {
        this.bookRepositoryTest = bookRepositoryTest;
        this.authorRepositoryTest = authorRepositoryTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);
        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author);
        bookRepositoryTest.save(book);

        System.out.println(book);
        Optional<Book> results =  bookRepositoryTest.findById(book.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(book);
    }

    @Test
    public void testThatManyBooksCanBeCreatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);

        Book book1 = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author);
        bookRepositoryTest.save(book1);

        Book book2 = TestDataUtils.createTestBook("978-1-2345-6789-1", "Beyond Horizon", author);
        bookRepositoryTest.save(book2);

        Book book3 = TestDataUtils.createTestBook("978-1-2345-6789-2", "The Last Ember", author);
        bookRepositoryTest.save(book3);

        Iterable<Book> results = bookRepositoryTest.findAll();
        assertThat(results).hasSize(3).containsExactly(book1, book2, book3);
    }

    @Test
    public void testThatBookCanBeUpdatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);

        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author);
        bookRepositoryTest.save(book);

        book.setTitle("Updated Title");
        bookRepositoryTest.save(book);

        Optional<Book> results = bookRepositoryTest.findById(book.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(book);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);

        Book book = TestDataUtils.createTestBook("978-1-2345-6789-0", "The Shadow in the Attic", author);
        bookRepositoryTest.save(book);

        bookRepositoryTest.deleteById(book.getIsbn());

        Optional<Book> result = bookRepositoryTest.findById(book.getIsbn());
        assertThat(result).isEmpty();
    }
}
