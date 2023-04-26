package com.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "Books")
public class GetSetBooks {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private int id;
    private String bookName;
    private String authorName;
    private String publication;
    private float price;
    private int availableCopies;
    private int rating;
    private int bookId;
}