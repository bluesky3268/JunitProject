package com.meta.junitproject.dto.response;

import com.meta.junitproject.domain.Book;
import lombok.Getter;

@Getter
public class BookRespDto {

    private Long id;
    private String title;
    private String author;


    public BookRespDto toBookRespDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        return this;
    }

    @Override
    public String toString() {
        return "BookRespDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
