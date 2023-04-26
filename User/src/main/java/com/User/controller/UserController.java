package com.User.controller;

import com.User.entity.GetBooks;
import com.User.entity.IssueBook;
import com.User.services.IssuedBooks;
import com.User.services.SequenceGeneratorService1;
import com.User.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.User.entity.IssueBook.SEQUENCE_NAME;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IssuedBooks issuedBooks;
    @Autowired
    private SequenceGeneratorService1 service;

    @GetMapping("/{bookid}")
    public List<GetBooks> getbook(@PathVariable("bookid") int id) {
        GetBooks book = new GetBooks();
        List IssuList = this.restTemplate.getForObject("http://localhost:8081/bookid/" + id, List.class);
        return IssuList;
    }

    @GetMapping("/showbooks")
    public ResponseEntity<List<GetBooks>> showAllR() {
        List IssuList = this.restTemplate.getForObject("http://localhost:8081/showAllR", List.class);
        return ResponseEntity.ok(IssuList);
    }

    @PostMapping("/issueBook/{id}")
    public ResponseEntity<?> issueBook(@RequestBody IssueBook issue, @PathVariable("id") int id) throws JsonProcessingException {
        issue.setId(service.getSequenceNumber(SEQUENCE_NAME));
        issue.setBookId(id);
        List IssuList = this.restTemplate.getForObject("http://localhost:8081/bookid/" + id, List.class);
        issue.setBooks(IssuList);
        IssueBook book1 = issuedBooks.save(issue);
        return ResponseEntity.ok(book1);
    }

    @GetMapping("/issuedata")
    public List<?> issued() {
        return issuedBooks.findAll();
    }
}