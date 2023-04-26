package com.User.services;

import com.User.entity.IssueBook;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IssuedBooks extends MongoRepository<IssueBook, Integer> {
}
