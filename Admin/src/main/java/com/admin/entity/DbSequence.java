package com.admin.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequence")
@Data
public class DbSequence {
    @Id
    private String id;
    private int seq;
}