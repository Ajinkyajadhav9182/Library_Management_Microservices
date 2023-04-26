package com.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "Books")
@AllArgsConstructor
public class GetBooks1 {
    private int id;
    private String bookName;
    private String authorName;
    private String publication;
    private float price;
    private int bookId;
}
