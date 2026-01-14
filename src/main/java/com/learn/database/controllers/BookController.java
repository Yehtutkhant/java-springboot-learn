package com.learn.database.controllers;

import com.learn.database.domain.dto.AuthorDto;
import com.learn.database.domain.dto.BookDto;
import com.learn.database.domain.entities.AuthorEntity;
import com.learn.database.domain.entities.BookEntity;

import com.learn.database.mapper.Mapper;
import com.learn.database.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final BookService bookService;
    private final Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn, @RequestBody BookDto book) {
        BookEntity bookEntity = bookMapper.mapFromBToA(book);
        BookEntity savedBookEntity = bookService.createBook(isbn, bookEntity);
        BookDto savedbookDto = bookMapper.mapFromAToB(savedBookEntity);
        return new ResponseEntity<BookDto>(savedbookDto, HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> listBooks() {
        List<BookEntity> bookEntityList = bookService.findAll();
        List<BookDto> bookDtoList = bookEntityList.stream().map(bookMapper::mapFromAToB).toList();

        return new ResponseEntity<List<BookDto>>(bookDtoList, HttpStatus.OK);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<BookDto> author(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> bookEntity = bookService.findOne(isbn);
        return bookEntity.map(entity-> {
                    BookDto bookDto = bookMapper.mapFromAToB(entity);
                    return new ResponseEntity<BookDto>(bookDto, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/book/{isbn}")
    public ResponseEntity<BookDto> fullUpdateAuthor(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookDto.setIsbn(isbn);
        BookEntity savedABookEntity = bookService.createBook(bookDto.getIsbn(), bookMapper.mapFromBToA(bookDto));
        BookDto savedAuthorDto = bookMapper.mapFromAToB(savedABookEntity);
        return new ResponseEntity<BookDto>(savedAuthorDto, HttpStatus.OK);
    }
}
