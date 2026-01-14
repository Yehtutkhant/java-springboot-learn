package com.learn.database;

import com.learn.database.domain.dto.AuthorDto;
import com.learn.database.domain.dto.BookDto;
import com.learn.database.domain.entities.AuthorEntity;
import com.learn.database.domain.entities.BookEntity;

public class TestDataUtils {

    public static AuthorEntity createTestAuthorEntity(String name, Integer age) {
        return AuthorEntity.builder().name(name).age(age).build();
    }

    public static BookEntity createTestBookEntity(String isbn, String title, AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn(isbn)
                .title(title)
                .authorEntity(authorEntity)
                .build();
    }

    public static AuthorDto createTestAuthorDto(String name, Integer age) {
        return AuthorDto.builder().name(name).age(age).build();
    }

    public static BookDto createTestBookDto(String isbn, String title, AuthorDto authorDto) {
        return BookDto.builder()
                .isbn(isbn)
                .title(title)
                .authorDto(authorDto)
                .build();
    }
}
