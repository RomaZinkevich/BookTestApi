package com.zirom.library.repositories;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import com.zirom.library.TestDataUtil;
import com.zirom.library.domain.entities.AuthorEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        underTest.save(author);
        Optional<AuthorEntity> result = underTest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        underTest.save(authorA);
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB.setId(null);
        underTest.save(authorB);
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();
        authorC.setId(null);
        underTest.save(authorC);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3).
                containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        AuthorEntity savedAuthor = underTest.save(authorA);
        authorA.setName("UPDATED");
        AuthorEntity updatedAuthor = underTest.save(authorA);
        Optional<AuthorEntity> result = underTest.findById(savedAuthor.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedAuthor);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        AuthorEntity savedAuthor = underTest.save(authorA);
        underTest.deleteById(savedAuthor.getId());
        Optional<AuthorEntity> result = underTest.findById(savedAuthor.getId());
        assertThat(result).isEmpty();
    }



}