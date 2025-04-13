package com.zirom.library.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zirom.library.TestDataUtil;
import com.zirom.library.domain.dto.BookDto;
import com.zirom.library.domain.entities.AuthorEntity;
import com.zirom.library.domain.entities.BookEntity;
import com.zirom.library.services.BookService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookService bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
            .content(createBookJson)
        ).andExpect(
            MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
            .content(createBookJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/books")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListBooksReturnsListOfBooks() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/books")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookEntity.getIsbn())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].title").value(testBookEntity.getTitle())
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/books/"+testBookEntity.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/authors/99")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetAuthorsReturnsAuthor() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/books/"+testBookEntity.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity.getIsbn())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        );
    }

    @Test
    public void testThatUpdateBookReturnsHttpStatus200Ok() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity =  bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDtoA(null);
        testBookA.setIsbn(savedBookEntity.getIsbn());
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/books/"+savedBookEntity.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJson)
        ).andExpect(
            MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatUpdateBookReturnsUpdatedBook() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity =  bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDtoB(null);
        testBookA.setIsbn(savedBookEntity.getIsbn());
        testBookA.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/books/" + savedBookEntity.getIsbn())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.isbn").value(savedBookEntity.getIsbn())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );
    }
}
