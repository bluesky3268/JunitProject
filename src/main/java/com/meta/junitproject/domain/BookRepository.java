package com.meta.junitproject.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository // -> JpaRepository를 상속 받으면 생략가능
public interface BookRepository extends JpaRepository<Book, Long> {
}
