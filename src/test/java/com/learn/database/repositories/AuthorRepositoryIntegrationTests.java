package com.learn.database.repositories;

import com.learn.database.TestDataUtils;
import com.learn.database.domain.Author;
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
public class AuthorRepositoryIntegrationTests {

    private final AuthorRepository authorRepositoryTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository authorRepositoryTest) {
        this.authorRepositoryTest = authorRepositoryTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);

        Optional<Author> result = authorRepositoryTest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatManyAuthorsCanBeCreatedAndRecalled() {

        Author authorA = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(authorA);

        Author authorB = TestDataUtils.createTestAuthor("Thomas Cronin", 44);
        authorRepositoryTest.save(authorB);

        Author authorC = TestDataUtils.createTestAuthor("Jessy A Casey", 24);
        authorRepositoryTest.save(authorC);

        Iterable<Author> results = authorRepositoryTest.findAll();
        assertThat(results).hasSize(3).containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);

        author.setName("New Name for Abigail");
        authorRepositoryTest.save(author);

        Optional<Author> result = authorRepositoryTest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {

        Author author = TestDataUtils.createTestAuthor("Abigail", 80);
        authorRepositoryTest.save(author);

        authorRepositoryTest.deleteById(author.getId());

        Optional<Author> result = authorRepositoryTest.findById(author.getId());
        assertThat(result).isEmpty();
    }
}
