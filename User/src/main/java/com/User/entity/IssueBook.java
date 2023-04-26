package com.User.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "Issued")
public class IssueBook {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private int id;
    private int BookId;
    private String readerName;
    private String address;
    private Long contactNo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate dateOfIssue = LocalDate.now();
    private int copies;
    private List<GetBooks> Books = new ArrayList<>();


    public IssueBook(int id, int bookId, String readerName, String address, Long contactNo, int copies) {
        this.id = id;
        BookId = bookId;
        this.readerName = readerName;
        this.address = address;
        this.contactNo = contactNo;
        this.copies = copies;
    }
}