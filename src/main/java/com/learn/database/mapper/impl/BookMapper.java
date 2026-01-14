package com.learn.database.mapper.impl;

import com.learn.database.domain.dto.BookDto;
import com.learn.database.domain.entities.BookEntity;
import com.learn.database.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapFromAToB(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFromBToA(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
