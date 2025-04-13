package com.zirom.library.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.zirom.library.domain.dto.BookDto;
import com.zirom.library.domain.entities.BookEntity;
import com.zirom.library.mappers.Mapper;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto>{
    private final ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapTo(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
