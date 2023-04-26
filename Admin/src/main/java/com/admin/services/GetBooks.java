package com.admin.services;


import com.admin.entity.GetBooks1;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GetBooks extends MongoRepository<GetBooks1, Integer> {
}


