package com.meta.junitproject.dto.request;

import com.meta.junitproject.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BookSaveReqDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotBlank
    @Size(min = 2, max = 20)
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(this.title)
                .author(this.author)
                .build();
    }

    @Override
    public String toString() {
        return "BookSaveReqDto{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
