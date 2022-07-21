package com.meta.junitproject.dto;

import com.meta.junitproject.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSaveReqDto {

    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(this.title)
                .author(this.author)
                .build();
    }
}
