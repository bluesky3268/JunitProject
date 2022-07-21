package com.meta.junitproject.domain;

import com.meta.junitproject.dto.BookSaveReqDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // -> DB와 관련된 컴포넌트만 메모리에 로딩 됨
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    // 등록 테스트
    @Test
    public void 등록테스트() {
        // given
        String title = "JUnit5";
        String author = "hyunbenny";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when
        Book bookPersistence = bookRepository.save(book);

        // then
//        assertThat(bookPersistence.getTitle()).isEqualTo("JUnit5");
//        assertThat(bookPersistence.getAuthor()).isEqualTo("hyunbenny");
        assertEquals(title, bookPersistence.getTitle());
        assertEquals(author, bookPersistence.getAuthor());
    }

    // 단건 조회 테스트
    @Test
    public void 단건조회테스트() {
        // given
        Long id = 1L;
        String title = "JUnit5";
        String author = "hyunbenny";

        // when

        // then
    }

    // 목록 조회 테스트

    // 수정 테스트

    // 삭제 테스트


}
