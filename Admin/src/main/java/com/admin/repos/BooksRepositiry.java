package com.admin.repos;


import com.admin.entity.GetSetBooks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BooksRepositiry extends MongoRepository<GetSetBooks, Integer> {
}
