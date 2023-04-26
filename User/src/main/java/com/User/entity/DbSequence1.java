package com.User.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequence1")
@Data
public class DbSequence1 {
    @Id
    private String id;
    private int seq;
}