package com.meta.junitproject.service;

import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.dto.BookRespDto;
import com.meta.junitproject.dto.BookSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto regist(BookSaveReqDto reqDto) {
        Book book = bookRepository.save(reqDto.toEntity());
        return new BookRespDto().toBookRespDto(book);
    }

    // 수정

    // 삭제

    // 목록

    // 단건 조회



}
