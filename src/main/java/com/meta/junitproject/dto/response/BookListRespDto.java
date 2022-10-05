package com.meta.junitproject.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookListRespDto {

    private List<BookRespDto> items = new ArrayList<>();

    @Builder
    public BookListRespDto(List<BookRespDto> bookList) {
        this.items = bookList;
    }
}
