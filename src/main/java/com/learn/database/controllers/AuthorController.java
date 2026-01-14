package com.learn.database.controllers;


import com.learn.database.domain.dto.AuthorDto;
import com.learn.database.domain.entities.AuthorEntity;
import com.learn.database.mapper.Mapper;
import com.learn.database.services.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthorController {

    private final ModelMapper modelMapper;
    private AuthorService authorService;

    private final Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper, ModelMapper modelMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
        this.modelMapper = modelMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFromBToA(author);
        AuthorEntity savedBookEntity = authorService.save(authorEntity);
        AuthorDto savedBookDto = authorMapper.mapFromAToB(savedBookEntity);
        return new ResponseEntity<AuthorDto>(savedBookDto, HttpStatus.CREATED);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDto>> listAuthors() {
        List<AuthorEntity> authorEntityList = authorService.findAll();
        List<AuthorDto> authorDtoList = authorEntityList.stream().map(authorMapper::mapFromAToB).toList();

        return new ResponseEntity<List<AuthorDto>>(authorDtoList, HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<AuthorDto> author(@PathVariable("id") Long id) {
        Optional<AuthorEntity> authorEntity = authorService.findOne(id);
        return authorEntity.map(entity-> {
                    AuthorDto authorDto = authorMapper.mapFromAToB(entity);
                    return new ResponseEntity<AuthorDto>(authorDto, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/author/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
        if(!authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorDto.setId(id);
        AuthorEntity savedAuthorEntity = authorService.save(authorMapper.mapFromBToA(authorDto));
        AuthorDto savedAuthorDto = authorMapper.mapFromAToB(savedAuthorEntity);
        return new ResponseEntity<AuthorDto>(savedAuthorDto, HttpStatus.OK);
    }
}
