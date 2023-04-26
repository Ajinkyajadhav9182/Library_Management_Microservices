package com.admin.controller;

import com.admin.entity.GetSetBooks;

import com.admin.entity.GetBooks1;
import com.admin.services.AdminService;
import com.admin.services.BooksRepositiry;
import com.admin.services.GetBooks;
import com.admin.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.admin.entity.GetSetBooks.SEQUENCE_NAME;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private BooksRepositiry booksRepositiry;
    @Autowired
    private SequenceGeneratorService service;
    @Autowired
    private GetBooks getBooks;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/bookid/{bookid}")
    public List<?> getBooks(@PathVariable("bookid") int id) {
        List<GetBooks1> getBook1s = new ArrayList<>();
        getBook1s.add(this.getBooks.findById(id).get());
        return getBook1s;
    }

    //adding a new book in db
    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody GetSetBooks books) {
        boolean isThere = this.booksRepositiry.existsById(books.getId());
        if (isThere) {
            return ResponseEntity.ok("This BookId Already Exist");
        }
        int a = service.getSequenceNumber(SEQUENCE_NAME);
        books.setId(a);
        books.setBookId(a);
        GetSetBooks saved = this.booksRepositiry.save(books);
        return ResponseEntity.ok(saved);
    }

    //show limited data to user
    @GetMapping("/showAllR")
    public ResponseEntity<List<GetBooks1>> showAllR() {
        List<GetBooks1> listBooks = this.getBooks.findAll();
        if (listBooks.size() == 0) {
            return new ResponseEntity("Books Not Available", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listBooks);
    }
    // show all data to admin

    @GetMapping("/showAll")
    public ResponseEntity<List<GetSetBooks>> showAll() {
        List<GetSetBooks> listBooks = this.booksRepositiry.findAll();
        if (listBooks.size() == 0) {
            return new ResponseEntity("Books Not Available", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listBooks);
    }

    // delete all records
    @DeleteMapping("/deleteAll")
    public String deleteAll() {
        this.booksRepositiry.deleteAll();
        return "All Books Deleted Successfully";
    }

    // delete single record
    @DeleteMapping("/delete/{id}")
    public String deleteSingle(@PathVariable("id") int id) {
        boolean isThere = booksRepositiry.existsById(id);
        if (isThere) {
            booksRepositiry.deleteById(id);
            return "Book Is Deleted";
        }
        return "Book Is Not Available";
    }

    //view single record
    @GetMapping("/show/{id}")
    public ResponseEntity<GetSetBooks> showSingle(@PathVariable("id") int id) {
        boolean isThere = booksRepositiry.existsById(id);
        if (isThere) {
            GetSetBooks books = null;
            books = booksRepositiry.findById(id).get();
            return ResponseEntity.ok(books);
        }
        return new ResponseEntity("Book Not Found In The Database", HttpStatus.NOT_FOUND);
    }

    // update a record
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody GetSetBooks books, @PathVariable("id") int id) {
        boolean isThere = booksRepositiry.existsById(id);
        if (isThere) {
            GetSetBooks book1 = this.booksRepositiry.findById(id).get();
            // GetSetBooks book2 = adOperation.update(book1, books);
            this.booksRepositiry.save(book1);
            return ResponseEntity.ok(book1);
        }
        return ResponseEntity.ok("This Id Is Not Present");
    }

    @GetMapping("/issued")
    public List<?> issued() {
        List IssuList = this.restTemplate.getForObject("http://localhost:8080/issuedata", List.class);
        return IssuList;
    }
}
