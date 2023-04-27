package com.User.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Reader Name Should Not Be Null")
    private String readerName;

    @NotEmpty(message = "Address Should Not Be Null")
    private String address;

    @Min(value = 1000000000L, message = "Mobile number should be at least 10 digits")
    @Max(value = 9999999999L, message = "Mobile number should be no more than 10 digits")
    private Long contactNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate dateOfIssue = LocalDate.now();

    @Min(value = 1, message = "Copies Should Be Greater Than 0")
    @Max(value = 2, message = "Copies Should Be Less Than 2")
    private int copies;

    private List<GetBooks> Books = new ArrayList<>();
}