package com.admin.controller;

import com.admin.entity.GetSetBooks;

import com.admin.entity.GetBooks1;
import com.admin.model.JwtRequest;
import com.admin.model.JwtResponse;
import com.admin.repos.BooksRepositiry;
import com.admin.repos.GetBooks;
import com.admin.services.AdminOperation;
import com.admin.services.SequenceGeneratorService;
import com.admin.services.UserService;
import com.admin.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.admin.entity.GetSetBooks.SEQUENCE_NAME;

@RestController
public class AdminController {
    @Autowired
    private BooksRepositiry booksRepositiry;
    @Autowired
    private SequenceGeneratorService service;
    @Autowired
    private GetBooks getBooks;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AdminOperation adOperation;
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

    @GetMapping("/bookid/{bookid}")
    public List<?> getBooks(@PathVariable("bookid") int id) {
        List<GetBooks1> getBook1s = new ArrayList<>();
        getBook1s.add(this.getBooks.findById(id).get());
        return getBook1s;
    }

    //show limited data to user
    @GetMapping("/showAllR")
    public ResponseEntity<List<GetBooks1>> showAllR() {
        List<GetBooks1> listBooks = this.getBooks.findAll();
        return ResponseEntity.ok(listBooks);
    }

    //adding a new book in db
    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@Valid @RequestBody GetSetBooks books) {
        int a = service.getSequenceNumber(SEQUENCE_NAME);
        books.setId(a);
        books.setBookId(a);
        GetSetBooks saved = this.booksRepositiry.save(books);
        return ResponseEntity.ok(saved);
    }

    // show all data to admin
    @GetMapping("/showAll")
    public ResponseEntity<List<GetSetBooks>> showAll() {
        if (this.booksRepositiry.findAll().size() == 0) {
            return new ResponseEntity("Books Not Available...", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(this.booksRepositiry.findAll());
    }

    // delete all records
    @DeleteMapping("/deleteAll")
    public String deleteAll() {
        this.booksRepositiry.deleteAll();
        return "All Books Deleted Successfully...";
    }

    // delete single record
    @DeleteMapping("/delete/{id}")
    public String deleteSingle(@PathVariable("id") int id) {
        boolean isThere = booksRepositiry.existsById(id);
        if (isThere) {
            booksRepositiry.deleteById(id);
            return "Book Is Deleted Successfully...";
        }
        return "Book Is Not Available";
    }

    //view single record
    @GetMapping("/show/{id}")
    public ResponseEntity<GetSetBooks> showSingle(@PathVariable("id") int id) {
        boolean isThere = booksRepositiry.existsById(id);
        if (isThere) {
            GetSetBooks books = booksRepositiry.findById(id).get();;
            return ResponseEntity.ok(books);
        }
        return new ResponseEntity("Book Not Found In The Database...", HttpStatus.NOT_FOUND);
    }

    // update a record
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody GetSetBooks books, @PathVariable("id") int id) {
        boolean isThere = booksRepositiry.existsById(id);
        if (isThere) {
            GetSetBooks book1 = this.booksRepositiry.findById(id).get();
            GetSetBooks book2 = adOperation.update(book1, books);
            this.booksRepositiry.save(book1);
            return ResponseEntity.ok(book1);
        }
        return ResponseEntity.ok("This Id Is Not Present...");
    }

    @GetMapping("/issued")
    public ResponseEntity<List> issued(@RequestBody String token) {
        String A = token.substring(15, token.length() - 3);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(A);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8080/issuedata", HttpMethod.GET, request, Object.class);
        Object responseBody = response.getBody();
        if (responseBody == null) {
            return new ResponseEntity("No One Issued Book...", HttpStatus.ACCEPTED);
        }
        return ResponseEntity.ok(Collections.singletonList(Optional.of(responseBody)));
    }
}