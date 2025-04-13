package com.zirom.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String isbn;

    private String title;

    private AuthorDto author;
}
