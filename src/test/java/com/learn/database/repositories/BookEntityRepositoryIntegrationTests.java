package com.learn.database.repositories;

import com.learn.database.TestDataUtils;
import com.learn.database.domain.entities.AuthorEntity;
import com.learn.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private final BookRepository bookRepositoryTest;

    // have to use this bean to save author to the db explicitly because saving author from cascading from the book has the id of null for authors
    private final AuthorRepository authorRepositoryTest;


    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository bookRepositoryTest, AuthorRepository authorRepositoryTest) {
        this.bookRepositoryTest = bookRepositoryTest;
        this.authorRepositoryTest = authorRepositoryTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);
        BookEntity bookEntity = TestDataUtils.createTestBookEntity("978-1-2345-6789-0", "The Shadow in the Attic", authorEntity);
        bookRepositoryTest.save(bookEntity);

        System.out.println(bookEntity);
        Optional<BookEntity> results =  bookRepositoryTest.findById(bookEntity.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatManyBooksCanBeCreatedAndRecalled() {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);

        BookEntity bookEntity1 = TestDataUtils.createTestBookEntity("978-1-2345-6789-0", "The Shadow in the Attic", authorEntity);
        bookRepositoryTest.save(bookEntity1);

        BookEntity bookEntity2 = TestDataUtils.createTestBookEntity("978-1-2345-6789-1", "Beyond Horizon", authorEntity);
        bookRepositoryTest.save(bookEntity2);

        BookEntity bookEntity3 = TestDataUtils.createTestBookEntity("978-1-2345-6789-2", "The Last Ember", authorEntity);
        bookRepositoryTest.save(bookEntity3);

        Iterable<BookEntity> results = bookRepositoryTest.findAll();
        assertThat(results).hasSize(3).containsExactly(bookEntity1, bookEntity2, bookEntity3);
    }

    @Test
    public void testThatBookCanBeUpdatedAndRecalled() {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);

        BookEntity bookEntity = TestDataUtils.createTestBookEntity("978-1-2345-6789-0", "The Shadow in the Attic", authorEntity);
        bookRepositoryTest.save(bookEntity);

        bookEntity.setTitle("Updated Title");
        bookRepositoryTest.save(bookEntity);

        Optional<BookEntity> results = bookRepositoryTest.findById(bookEntity.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);

        BookEntity bookEntity = TestDataUtils.createTestBookEntity("978-1-2345-6789-0", "The Shadow in the Attic", authorEntity);
        bookRepositoryTest.save(bookEntity);

        bookRepositoryTest.deleteById(bookEntity.getIsbn());

        Optional<BookEntity> result = bookRepositoryTest.findById(bookEntity.getIsbn());
        assertThat(result).isEmpty();
    }
}
