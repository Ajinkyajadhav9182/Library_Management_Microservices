package com.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Document(collection = "Books")
public class GetSetBooks {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private int id;

    @NotNull(message = "Book Name Should Not Be Null")
    private String bookName;

    @NotNull(message = "Author Name Should Not Be Null")
    private String authorName;

    @NotNull(message = "Publication Should Not Be Null")
    private String publication;

    @Min(value = 1, message = "Price Should Be Greater Than 0")
    private float price;

    @Min(value = 1, message = "Available Copies Should Be Greater Than 0")
    private int availableCopies;

    private int rating;
    private int bookId;
}