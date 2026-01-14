package com.learn.database.controllers;


import com.learn.database.TestDataUtils;
import com.learn.database.domain.dto.AuthorDto;
import com.learn.database.domain.entities.AuthorEntity;
import com.learn.database.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final AuthorService authorService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }


    @Test
    public void testThatCreateAuthorReturnsHttp201Created() throws Exception {
        AuthorDto author = TestDataUtils.createTestAuthorDto("Abigail Rose", 88);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        . content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorReturnsSavedAuthor() throws Exception {
        AuthorDto author = TestDataUtils.createTestAuthorDto("Abigail Rose", 88);
        String authorJson = objectMapper.writeValueAsString(author);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        . content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(88)
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpOK() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail Rose", 80);
        authorService.save(authorEntity);
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
    public void testThatAuthorReturnsHttpOKWhenExists() throws Exception {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail Rose", 80);
        authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatAuthorReturnsHttpNotFoundWhenDoesntExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatAuthorsReturnAuthorWhenExists() throws Exception {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail Rose", 80);
        authorService.save(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpNotFoundWhenDoesntExist() throws Exception {

        AuthorDto authorDto = TestDataUtils.createTestAuthorDto("Abigail Rose", 88);
        String authorJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/author/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpOKWhenExists() throws Exception {

        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail Rose", 88);
        authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtils.createTestAuthorDto("Abigail Rose", 88);
        String authorJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/author/" + authorEntity.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnUpdatedAuthorWhenExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtils.createTestAuthorEntity("Abigail Rose", 88);
        authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtils.createTestAuthorDto("Updated Name", 20);
        authorDto.setId(authorEntity.getId());
        String authorJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/author/" + authorEntity.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }
}
