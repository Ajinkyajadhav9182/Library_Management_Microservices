package com.User.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "Books")
@AllArgsConstructor
@NoArgsConstructor
public class GetBooks {
    @Id
    private int id;
    private String bookName;
    private String authorName;
    private String publication;
    private float price;
    private int bookId;
}
