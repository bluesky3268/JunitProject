package com.meta.junitproject.service;

import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.dto.response.BookListRespDto;
import com.meta.junitproject.dto.response.BookRespDto;
import com.meta.junitproject.dto.request.BookSaveReqDto;
import com.meta.junitproject.util.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MailSender mailSender;

    // 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto saveBook(BookSaveReqDto reqDto) {
        Book book = bookRepository.save(reqDto.toEntity());

        // 등록시, 등록 알림 메일 발송
        if (book != null) {
            if (!mailSender.send()) {
                log.info("[메일 발송 실패]");
                throw new RuntimeException("메일 발송에 실패했습니다.");
            }
            log.info("[메일 발송 성공]");
        }

        return new BookRespDto().toBookRespDto(book);
    }

    // 수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto updateBook(Long id, BookSaveReqDto dto) {
        Book findBook = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
        findBook.updateBook(dto);
        return new BookRespDto().toBookRespDto(findBook);
    }

    // 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBook(Long id) {
        // null체크는 컨트롤러에서..
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("해당 책이 존재하지 않습니다.");
        }
    }

    // 목록
    public BookListRespDto getBookList() {
        List<BookRespDto> findBooks = bookRepository.findAll().stream().map((book) -> new BookRespDto().toBookRespDto(book)).collect(Collectors.toList());
        return new BookListRespDto().builder().bookList(findBooks).build();
    }

    // 단건 조회
    public BookRespDto getBook(Long id) {
        Book findBook = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
        return new BookRespDto().toBookRespDto(findBook);
    }


}
