package com.learn.database;

import com.learn.database.domain.Author;
import com.learn.database.domain.Book;

public class TestDataUtils {

    public static Author createTestAuthor(String name, Integer age) {
        return Author.builder().name(name).age(age).build();
    }

    public static Book createTestBook(String isbn, String title, Author author) {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .build();
    }
}
