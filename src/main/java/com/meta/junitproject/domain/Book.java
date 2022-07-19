package com.meta.junitproject.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Book {

    @Id
    private long id;

    @Column(length = 50, nullable = true)
    private String title;

    @Column(length = 20, nullable = true)
    private String author;

    @Builder
    public Book(long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

}
