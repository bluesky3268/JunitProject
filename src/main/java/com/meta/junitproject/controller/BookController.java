package com.meta.junitproject.controller;

import com.meta.junitproject.dto.response.BookListRespDto;
import com.meta.junitproject.dto.response.BookRespDto;
import com.meta.junitproject.dto.request.BookSaveReqDto;
import com.meta.junitproject.dto.response.CommonRespDto;
import com.meta.junitproject.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    // 등록
    @PostMapping("/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto saveReqDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                error.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new IllegalArgumentException(error.toString());
        }

        BookRespDto bookRespDto = bookService.saveBook(saveReqDto);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("저장 성공").body(bookRespDto).build()
                , HttpStatus.CREATED); // 201
    }

    // 수정
    @PutMapping("/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto saveReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                error.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new IllegalArgumentException(error.toString());
        }

        BookRespDto bookRespDto = bookService.updateBook(id, saveReqDto);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("수정 성공").body(bookRespDto).build()
                , HttpStatus.OK); // 201
    }

    // 삭제
    @DeleteMapping("/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("삭제 성공").body(null).build(), HttpStatus.OK);
    }

    // 목록 조회
    @GetMapping("/v1/book")
    public ResponseEntity<?> getBookList() {
        BookListRespDto bookList = bookService.getBookList();
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("목록 조회 성공").body(bookList).build(), HttpStatus.OK);
    }

    // 단건 조회
    @GetMapping("/v1/book/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        BookRespDto book = bookService.getBook(id);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("단건 조회 성공").body(book).build(), HttpStatus.OK);
    }

}
