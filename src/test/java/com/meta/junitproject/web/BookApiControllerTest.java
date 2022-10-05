package com.meta.junitproject.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.meta.junitproject.domain.Book;
import com.meta.junitproject.dto.request.BookSaveReqDto;
import com.meta.junitproject.dto.response.BookRespDto;
import com.meta.junitproject.service.BookService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 컨트롤러 테스트 => 통합테스트
 * 컨트롤러만 테스트하려면 Mock을 사용해서 테스트하면 된다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static ObjectMapper objectMapper;

    private static MockMvc mockMvc;

    private static HttpHeaders headers;

    @Test
    public void DITest() {
        assertThat(bookService).isNotNull();
        assertThat(testRestTemplate).isNotNull();
    }

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void 책저장성공테스트() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트");
        saveReqDto.setAuthor("TESTER");

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

    @Test
    public void 책저장실패테스트_title가null() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
//        saveReqDto.setTitle("테스트");
        saveReqDto.setAuthor("TESTER");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());
        System.out.println("==========================================");
        System.out.println(response.getBody().toString());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{title=공백일 수 없습니다}");
        assertThat(respBody).isNull();

    }

    @Test
    public void 책저장실패테스트_title길이초과() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트");
        saveReqDto.setAuthor("TESTER");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());
        System.out.println("==========================================");
        System.out.println(response.getBody().toString());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{title=크기가 1에서 50 사이여야 합니다}");
        assertThat(respBody).isNull();

    }

    @Test
    public void 책저장실패테스트_author가null() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트");
//        saveReqDto.setAuthor("TESTER");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());
        System.out.println("==========================================");
        System.out.println(response.getBody().toString());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{author=공백일 수 없습니다}");
        assertThat(respBody).isNull();

    }

    @Test
    public void 책저장실패테스트_author길이초과() throws Exception {
        //given
        BookSaveReqDto saveReqDto = new BookSaveReqDto();
        saveReqDto.setTitle("테스트");
        saveReqDto.setAuthor("TESTERTESTERTESTERTESTERTESTERTESTERTESTERTESTERTESTERTESTERTESTERTESTER");

        String body = objectMapper.writeValueAsString(saveReqDto);

        // when
//        mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON).content(body));

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =  testRestTemplate.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext docContext = JsonPath.parse(response.getBody());
        System.out.println("==========================================");
        System.out.println(response.getBody().toString());

        HttpStatus statusCode = response.getStatusCode();
        String message = docContext.read("$.message");
        String respBody = docContext.read("$.body");

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualTo("{author=크기가 2에서 20 사이여야 합니다}");
        assertThat(respBody).isNull();

    }
}
