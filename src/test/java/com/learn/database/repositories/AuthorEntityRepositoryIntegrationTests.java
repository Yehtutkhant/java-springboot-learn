package com.learn.database.repositories;

import com.learn.database.TestDataUtils;
import com.learn.database.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private final AuthorRepository authorRepositoryTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository authorRepositoryTest) {
        this.authorRepositoryTest = authorRepositoryTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);

        Optional<AuthorEntity> result = authorRepositoryTest.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatManyAuthorsCanBeCreatedAndRecalled() {

        AuthorEntity authorEntityA = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntityA);

        AuthorEntity authorEntityB = TestDataUtils.createTestAuthorEntity("Thomas Cronin", 44);
        authorRepositoryTest.save(authorEntityB);

        AuthorEntity authorEntityC = TestDataUtils.createTestAuthorEntity("Jessy A Casey", 24);
        authorRepositoryTest.save(authorEntityC);

        Iterable<AuthorEntity> results = authorRepositoryTest.findAll();
        assertThat(results).hasSize(3).containsExactly(authorEntityA, authorEntityB, authorEntityC);
    }

    @Test
    public void testThatAuthorCanBeUpdatedAndRecalled() {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);

        authorEntity.setName("New Name for Abigail");
        authorRepositoryTest.save(authorEntity);

        Optional<AuthorEntity> result = authorRepositoryTest.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail", 80);
        authorRepositoryTest.save(authorEntity);

        authorRepositoryTest.deleteById(authorEntity.getId());

        Optional<AuthorEntity> result = authorRepositoryTest.findById(authorEntity.getId());
        assertThat(result).isEmpty();
    }
}
