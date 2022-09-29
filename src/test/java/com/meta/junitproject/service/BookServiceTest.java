package com.meta.junitproject.service;

import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.dto.BookRespDto;
import com.meta.junitproject.dto.BookSaveReqDto;
import com.meta.junitproject.util.MailSender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class) // 가짜 메모리 환경 생성
public class BookServiceTest {

    /**
     * 서비스 레이어를 테스트 할 때는 서비스 레이어만 테스트 해야 한다.
     * 이미 테스트가 끝난 리포지토리 레이어까지 굳이 같이 테스트할 필요없음.
     */

    @InjectMocks // BookService객체가 힙 메모리에 뜰 때 @Mock이나 @Spy가 붙은 애들을 주입한다.
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    @Test
    public void 책등록테스트() {

        // given
        BookSaveReqDto saveDto = new BookSaveReqDto();
        saveDto.setTitle("JUNIT TEST");
        saveDto.setAuthor("hyunbin");

        // stub
        // bookRepository.save(), mailSender.send()가 호출될 때 기대되는 값을 내가 미리 정의
        when(bookRepository.save(any())).thenReturn(saveDto.toEntity());
        when(mailSender.send()).thenReturn(true);

        // when
        BookRespDto savedBook = bookService.saveBook(saveDto);

        // then
        assertThat(savedBook.getTitle()).isEqualTo(saveDto.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(saveDto.getAuthor());

    }
}
