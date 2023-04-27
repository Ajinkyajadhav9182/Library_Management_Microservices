package com.User.controller;

import com.User.entity.GetBooks;
import com.User.entity.IssueBook;
import com.User.handler.GetBooks11;
import com.User.repos.IssuedBooks;
import com.User.services.SequenceGeneratorService1;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.User.entity.IssueBook.SEQUENCE_NAME;

@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IssuedBooks issuedBooks;
    @Autowired
    private SequenceGeneratorService1 service;
    @Autowired
    private GetBooks11 books11;

    @GetMapping("/{bookid}")
    public ResponseEntity<List<GetBooks>> getbook(@PathVariable("bookid") int id) {
        try {
            List IssuList = this.restTemplate.getForObject("http://localhost:8081/bookid/" + id, List.class);
            if (IssuList.size() == 0) {
                return new ResponseEntity("This Book Id Not Available ", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(IssuList);
        } catch (Exception e) {
            return new ResponseEntity("This Book Id Not Available ", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/showbooks")
    public ResponseEntity<List<?>> showAllR() {
        try {
            List IssuList = this.restTemplate.getForObject("http://localhost:8081/showAllR", List.class);
            if (IssuList.size() == 0) {
                return new ResponseEntity("Books Not Available", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(IssuList);
        } catch (Exception e) {
            return new ResponseEntity("Books Not Available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/issueBook/{id}")
    public ResponseEntity<?> issueBook(@Valid @RequestBody IssueBook issue, @PathVariable("id") int id) throws JsonProcessingException {
        int getCopies = 0;
        boolean isThere = books11.existsById(id);
        if (isThere) {
            int getid = 0;
            List<IssueBook> listIssued = this.issuedBooks.findAll().stream().filter(c -> c.getReaderName().equals(issue.getReaderName())).collect(Collectors.toList());
            if (listIssued.size() == 1) {
                for (IssueBook k : listIssued) {
                    getCopies = k.getCopies();
                    getid = k.getId();
                }
            }
            if (listIssued.size() == 0) {
                issue.setId(service.getSequenceNumber(SEQUENCE_NAME));
                issue.setBookId(id);
                List IssuList = this.restTemplate.getForObject("http://localhost:8081/bookid/" + id, List.class);
                issue.setBooks(IssuList);
                IssueBook book1 = issuedBooks.save(issue);
                return ResponseEntity.ok(book1);
            }
            if (getCopies < 2 && listIssued.size() < 2) {
                if (issue.getCopies() == 1) {
                    issue.setId(getid);
                    issue.setBookId(id);
                    List IssuList = this.restTemplate.getForObject("http://localhost:8081/bookid/" + id, List.class);
                    issue.setBooks(IssuList);
                    IssueBook book1 = issuedBooks.save(issue);
                    return ResponseEntity.ok(book1);
                }
                return ResponseEntity.ok("You Have Already Issued 1 Book Now You Can Issue Only 1 Book ");
            }
            return ResponseEntity.ok("You Have Already Issued Two Books ");
        }
        return ResponseEntity.ok("This Book Id Not Available");
    }

    @GetMapping("/issuedata")
    public List<IssueBook> issued() {
        if (issuedBooks.findAll().size() == 0) {
            return null;
        }
        return issuedBooks.findAll();
    }
}