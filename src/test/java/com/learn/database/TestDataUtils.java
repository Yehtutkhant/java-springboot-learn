package com.learn.database;

import com.learn.database.domain.Author;
import com.learn.database.domain.Book;

public class TestDataUtils {

    public static Author createTestAuthor(long id, String name, int age) {
        return Author.builder().id(id).name(name).age(age).build();
    }

    public static Book createTestBook(String isbn, String title, long authorId) {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .authorId(authorId)
                .build();
    }
}
