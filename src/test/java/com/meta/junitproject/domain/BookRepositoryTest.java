package com.meta.junitproject.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@ActiveProfiles("dev")
@DataJpaTest // -> DB와 관련된 컴포넌트만 메모리에 로딩 됨
/**
 * @Rollback : 해당 메서드에 @Rollback(false) 를 선언해주지 않는 이상 기본값으로 메서드 실행 후 롤백 수행
 */
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void 필요데이터삽입() {
        Book bookA = Book.builder()
                .title("JUnit4")
                .author("hyunbin4")
                .build();

        Book bookB = Book.builder()
                .title("JUnit5")
                .author("hyunbin5")
                .build();

        Book bookC = Book.builder()
                .title("JUnit6")
                .author("hyunbin6")
                .build();

        bookRepository.save(bookA);
        bookRepository.save(bookB);
        bookRepository.save(bookC);
    }

    // 등록 테스트
    @Test
    public void 등록테스트() {
        // given
        String title = "JUnit5";
        String author = "hyunbin";
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
    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 단건조회테스트() {
        // given
        String title = "JUnit4";
        String author = "hyunbin4";

        // when
        Book findBookA = bookRepository.findById(1L).get();
        Book findBookB = bookRepository.findByTitle(title);

        // then
        assertThat(findBookA.getTitle()).isEqualTo(title);
        assertThat(findBookA.getAuthor()).isEqualTo(author);

        assertThat(findBookB.getId()).isEqualTo(1L);
        assertThat(findBookB.getAuthor()).isEqualTo(author);
    }

    // 목록 조회 테스트
    @Test
    public void 목록조회테스트() {
        //given
        String title1 = "JUnit4";
        String author1 = "hyunbin4";

        String title2 = "JUnit5";
        String author2 = "hyunbin5";

        String title3 = "JUnit6";
        String author3 = "hyunbin6";

        // when
        List<Book> books = bookRepository.findAll();

        //then
        assertThat(3).isEqualTo(books.size());

        assertThat(books.get(0).getTitle()).isNotEqualTo("JUnit3");
        assertThat(books.get(0).getAuthor()).isNotEqualTo("hyunbin3");

        assertEquals(title1, books.get(0).getTitle());
        assertEquals(author1, books.get(0).getAuthor());
        assertEquals(title2, books.get(1).getTitle());
        assertEquals(author2, books.get(1).getAuthor());
        assertEquals(title3, books.get(2).getTitle());
        assertEquals(author3, books.get(2).getAuthor());

    }

    // 수정 테스트
    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 수정테스트() {
        //given
        Long id = 1L;
        String title = "JUnit5_수정";
        String author = "hyunbin5_수정";

        //when
        Book editBook = Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .build();

        Book bookPs = bookRepository.save(editBook);// id = 1이 있으면 업데이트가 됨 -> JPA의 더티체킹

        //then
        assertEquals(id, bookPs.getId());
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }

    // 삭제 테스트
    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 삭제테스트() {
        /**
         * PK의 auto_increment값은 초기화가 안됨
         * -> @Transcational 은 모든 ROW를 삭제하여 truncate와 같은 효과를 볼 수 있지만 (테이블에 대해 롤백 작업)
         * auto-increment에 대한 롤백은 되지 않음
         * 그래서 @Sql()을 이용하여 테이블을 drop했다가 다시 create하는 방법을 사용함
         * -> id를 사용하는 로직의 테스트에는 붙여서 사용하는 것이 좋음
         */
        //given
        Long id = 1L;

        //when
        bookRepository.deleteById(id);

        //then
        Optional<Book> bookPs = bookRepository.findById(id);
        // Optional은 null이 될 수도 있고 값을 가지고 있을 수도 있음(값이 바로 null을 가지는 것을 방지하기 위해 Optional을 사용)
        // bookPs.isPresent() : null이 아닐 경우 true 리턴
        assertFalse(bookPs.isPresent());
        assertTrue(bookPs.isEmpty());

        // 인라인 리팩토링 하면
        assertFalse(bookRepository.findById(id).isPresent());

    }


}
