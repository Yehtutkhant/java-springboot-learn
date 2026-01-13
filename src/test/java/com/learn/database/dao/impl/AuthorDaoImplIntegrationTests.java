package com.learn.database.dao.impl;

import com.learn.database.TestDataUtils;
import com.learn.database.domain.Author;
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
public class AuthorDaoImplIntegrationTests {

    private final AuthorDaoImpl authorDaoTest;

    @Autowired
    public AuthorDaoImplIntegrationTests(AuthorDaoImpl authorDao) {
        this.authorDaoTest = authorDao;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(author);

        Optional<Author> results = authorDaoTest.findOne(author.getId());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(author);
    }

    @Test
    public void testThatManyAuthorsCanBeCreatedAndRecalled() {

        Author authorA = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(authorA);

        Author authorB = TestDataUtils.createTestAuthor(2L, "Thomas Cronin", 44);
        authorDaoTest.create(authorB);

        Author authorC = TestDataUtils.createTestAuthor(3L, "Jessy A Casey", 24);
        authorDaoTest.create(authorC);

        List<Author> results = authorDaoTest.find();
        assertThat(results).hasSize(3).containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdatedAndRecalled() {

        Author author = TestDataUtils.createTestAuthor(1L, "Abigail", 80);
        authorDaoTest.create(author);

        author.setName("New Name for Abigail");
        authorDaoTest.update(author.getId(), author);

        Optional<Author> result = authorDaoTest.findOne(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }
}
