package com.meta.junitproject.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * 컨트롤러 테스트 => 통합테스트
 * 컨트롤러만 테스트하려면 Mock을 사용해서 테스트하면 된다.
 */

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;


    private static ObjectMapper objectMapper;


    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

//    @BeforeEach
//    public void prepareData() {
//        String title = "테스트1";
//        String author = "Tester1";
//        Book book = Book.builder()
//                .title(title)
//                .author(author)
//                .build();
//        bookRepository.save(book);
//    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책저장성공테스트() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트2");
        saveReqDto.setAuthor("TESTER2");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());
        String title = docContext.read("$.body.title");
        String author = docContext.read("$.body.author");

        assertThat(title).isEqualTo(saveReqDto.getTitle());
        assertThat(author).isEqualTo(saveReqDto.getAuthor());
    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책저장실패테스트_title가null() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
//        saveReqDto.setTitle("테스트2");
        saveReqDto.setAuthor("TESTER2");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{title=공백일 수 없습니다}");
        assertThat(respBody).isNull();

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책저장실패테스트_title길이초과() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2테스트2");
        saveReqDto.setAuthor("TESTER2");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{title=크기가 1에서 50 사이여야 합니다}");
        assertThat(respBody).isNull();

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책저장실패테스트_author가null() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트2");
//        saveReqDto.setAuthor("TESTER2");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{author=공백일 수 없습니다}");
        assertThat(respBody).isNull();

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책저장실패테스트_author길이초과() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트2");
        saveReqDto.setAuthor("TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2TESTER2");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{author=크기가 2에서 20 사이여야 합니다}");
        assertThat(respBody).isNull();

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책수정성공() throws Exception {
        // given
        //데이터 삽입
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("TEST1");
        bookSaveReqDto.setAuthor("TESTER1");

        bookRepository.save(bookSaveReqDto.toEntity());

        // 수정할 데이터
        BookSaveReqDto requestDto = new BookSaveReqDto();
        requestDto.setTitle("TEST1_수정");
        requestDto.setAuthor("TESTER1_수정");

        String request = objectMapper.writeValueAsString(requestDto);

        Integer id = 1;
        // expected
        mockMvc.perform(put("/api/v1/book/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.message", is("수정 성공")))
                .andExpect(jsonPath("$.body.title", is(requestDto.getTitle())))
                .andExpect(jsonPath("$.body.author", is(requestDto.getAuthor())))
                .andDo(print());

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책삭제성공() throws Exception {
        // given
        List<Book> request = IntStream.range(1, 6)
                .mapToObj(i -> Book.builder()
                        .title("테스트" + i)
                        .author("tester" + i)
                        .build()).collect(Collectors.toList());
        bookRepository.saveAll(request);

        Integer id = 1;

        // expected
        mockMvc.perform(delete("/api/v1/book/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.message", is("삭제 성공")))
                .andExpect(jsonPath("$.body").isEmpty())
                .andDo(print());

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책삭제실패_없는bookId전달() throws Exception {
        // given
        List<Book> request = IntStream.range(1, 6)
                .mapToObj(i -> Book.builder()
                        .title("테스트" + i)
                        .author("tester" + i)
                        .build()).collect(Collectors.toList());
        bookRepository.saveAll(request);

        Integer id = 10;

        // expected
        mockMvc.perform(delete("/api/v1/book/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(2)))
                .andExpect(jsonPath("$.message", is("해당 책이 존재하지 않습니다.")))
                .andExpect(jsonPath("$.body").isEmpty())
                .andDo(print());

    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책목록조회성공() throws Exception {
        // given
        List<Book> request = IntStream.range(1, 11)
                .mapToObj(i -> Book.builder()
                        .title("테스트" + i)
                        .author("tester" + i)
                        .build()).collect(Collectors.toList());
        bookRepository.saveAll(request);

        // expected
        mockMvc.perform(get("/api/v1/book").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.message", is("목록 조회 성공")))
                .andExpect(jsonPath("$.body.items.length()", is(10)))
                .andExpect(jsonPath("$.body.items[0].title", is("테스트1")))
                .andExpect(jsonPath("$.body.items[0].author", is("tester1")))
                .andExpect(jsonPath("$.body.items[9].title", is("테스트10")))
                .andExpect(jsonPath("$.body.items[9].author", is("tester10")))
                .andDo(print());
    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책단건조회성공() throws Exception {
        // given
        List<Book> request = IntStream.range(1, 11)
                .mapToObj(i -> Book.builder()
                        .title("테스트" + i)
                        .author("tester" + i)
                        .build()).collect(Collectors.toList());
        bookRepository.saveAll(request);

        // expected
        mockMvc.perform(get("/api/v1/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.message", is("단건 조회 성공")))
                .andExpect(jsonPath("$.body.id", is(1)))
                .andExpect(jsonPath("$.body.title", is("테스트1")))
                .andExpect(jsonPath("$.body.author", is("tester1")))
                .andDo(print());
    }

    @Sql("classpath:testdb/bookTableReset.sql")
    @Test
    public void 책단건조회_해당값없음() throws Exception {
        // given
        List<Book> request = IntStream.range(1, 11)
                .mapToObj(i -> Book.builder()
                        .title("테스트" + i)
                        .author("tester" + i)
                        .build()).collect(Collectors.toList());
        bookRepository.saveAll(request);

        // expected
        mockMvc.perform(get("/api/v1/book/20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(2)))
                .andExpect(jsonPath("$.message", is("해당 책이 존재하지 않습니다.")))
                .andExpect(jsonPath("$.body").isEmpty())
                .andDo(print());
    }
}
