package com.meta.junitproject.controller;

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
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;

    // 등록
    @PostMapping("/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto saveReqDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                error.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new IllegalArgumentException(error.toString());
        }

        BookRespDto bookRespDto = bookService.saveBook(saveReqDto);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("성공적으로 저장했습니다.").body(bookRespDto).build()
                , HttpStatus.CREATED); // 201
    }

    // 수정
    @PatchMapping("/book")
    public ResponseEntity<?> updateBook() {
        return null;
    }

    // 삭제
    @DeleteMapping("/book")
    public ResponseEntity<?> deleteBook() {
        return null;
    }

    // 목록 조회
    @GetMapping("/book")
    public ResponseEntity<?> getBookList() {
        return null;
    }

    // 단건 조회
    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        return null;
    }
}
