package com.meta.junitproject.service;

import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.dto.response.BookRespDto;
import com.meta.junitproject.dto.request.BookSaveReqDto;
import com.meta.junitproject.util.MailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void 책수정테스트() {
        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit수정테스트");
        dto.setAuthor("hyunbin수정테스트");

        // stub
        Book findBook = new Book(1L, "junit", "hyunbin");
        when(bookRepository.findById(1L)).thenReturn(of(findBook));

        // when
        BookRespDto updatedBook = bookService.updateBook(1L, dto);

        // then
        assertThat(updatedBook.getTitle()).isEqualTo(dto.getTitle());
        assertThat(updatedBook.getAuthor()).isEqualTo(dto.getAuthor());

    }

    @Test
    public void 책삭제테스트() {
        // given
        Long id = 1L;
        Book book = new Book(id, "junit", "hyunbin");
        bookRepository.save(book);

        // when
        bookService.deleteBook(id);

        // then
        boolean empty = bookRepository.findById(book.getId()).isEmpty();
        assertThat(empty).isTrue();

    }

    @Test
    public void 책목록조회테스트() {
        // given

        // stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit1", "hyunbin1"));
        books.add(new Book(2L, "junit2", "hyunbin2"));

        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<BookRespDto> findBookList = bookService.getBookList();
        for (BookRespDto bookResp : findBookList) {
            System.out.println(bookResp.toString());
        }

        // then
        assertThat(findBookList.get(0).getTitle()).isEqualTo(books.get(0).getTitle());
        assertThat(findBookList.get(0).getAuthor()).isEqualTo(books.get(0).getAuthor());
        assertThat(findBookList.get(1).getTitle()).isEqualTo(books.get(1).getTitle());
        assertThat(findBookList.get(1).getAuthor()).isEqualTo(books.get(1).getAuthor());
    }

    @Test
    public void 책단건조회실패테스트() {
        // given
        Book book = new Book(1L, "junit1", "hyunbin");
        Long id = 2L;

        // stub
        when(bookRepository.findById(id)).thenThrow(new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBook(2L);
        });

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBook(2L);
        }, "해당 책이 존재하지 않습니다.");

        assertThat("해당 책이 존재하지 않습니다.").isEqualTo(exception.getMessage());

    }

    @Test
    public void 책단건조회성공테스트() {
        // given
        Book book = new Book(1L, "junit1", "hyunbin");
        Long id = 1L;

        // stub
        when(bookRepository.findById(id)).thenReturn(of(book));

        // when
        BookRespDto findBook = bookService.getBook(1L);

        // then
        assertThat(findBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(findBook.getAuthor()).isEqualTo(book.getAuthor());
    }

}
