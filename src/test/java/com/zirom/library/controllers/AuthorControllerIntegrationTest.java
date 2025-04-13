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
import com.zirom.library.domain.dto.AuthorDto;
import com.zirom.library.domain.entities.AuthorEntity;
import com.zirom.library.services.AuthorService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private final AuthorService authorService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(
            MockMvcRequestBuilders.post("/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(authorJson)
        ).andExpect(
            MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(
            MockMvcRequestBuilders.post("/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(authorJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value("80")
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/authors")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        authorService.save(testAuthorEntity);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/authors")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        authorService.save(testAuthorEntity);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/authors/"+testAuthorEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus404() throws Exception {
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
        authorService.save(testAuthorEntity);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/authors/"+testAuthorEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus404() throws Exception {
        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/authors/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(authorJson)
        ).andExpect(
            MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus200() throws Exception {
        AuthorEntity testAuthorEntity =  TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        AuthorEntity saved = authorService.save(testAuthorEntity);
        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/authors/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(authorJson)
        ).andExpect(
            MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatUpdateAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity testAuthorEntity =  TestDataUtil.createTestAuthorA();
        testAuthorEntity.setId(null);
        AuthorEntity saved = authorService.save(testAuthorEntity);
        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoB();
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/authors/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(authorJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").value(saved.getId())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("Thomas Cronin")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(44)
        );
    }
}
