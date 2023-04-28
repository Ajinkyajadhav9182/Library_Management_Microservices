package com.User.controller;

import com.User.entity.GetBooks;
import com.User.entity.IssueBook;
import com.User.handler.GetBooks11;
import com.User.model.JwtRequest;
import com.User.model.JwtResponse;
import com.User.repos.IssuedBooks;
import com.User.services.ReaderOperation;
import com.User.services.SequenceGeneratorService1;
import com.User.services.UserService;
import com.User.utility.JWTUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.User.entity.IssueBook.SEQUENCE_NAME;


@RestController
@EnableEurekaClient
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IssuedBooks issuedBooks;
    @Autowired
    private SequenceGeneratorService1 service;
    @Autowired
    private GetBooks11 books11;
    @Autowired
    private ReaderOperation operation;
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

    @GetMapping("/{bookid}")
    public ResponseEntity<?> getbook(@RequestBody String Token, @PathVariable("bookid") int id) {
        try {
            String A = Token.substring(15, Token.length() - 3);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(A);
            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8081/bookid/" + id, HttpMethod.GET, request, Object.class);
            Object responseBody = response.getBody();
            if (responseBody == null) {
                return new ResponseEntity("This Book Id Not Available...", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return new ResponseEntity("This Book Id Not Available...", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/showbooks")
    public ResponseEntity<List<?>> showAllR(@RequestBody String Token) {
        try {
            String A = Token.substring(15, Token.length() - 3);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(A);
            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8081/showAllR", HttpMethod.GET, request, Object.class);
            Object responseBody = response.getBody();
            if (responseBody == null) {
                return new ResponseEntity("Books Not Available...", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(Collections.singletonList(Optional.of(responseBody)));
        } catch (Exception e) {
            return new ResponseEntity("Books Not Available...", HttpStatus.NOT_FOUND);
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
                    IssueBook books = issuedBooks.findById(getid).get();
                    List IssuList = this.restTemplate.getForObject("http://localhost:8081/bookid/" + id, List.class);
                    List<GetBooks> getBooks = books.getBooks();
                    for (Object v : getBooks) {
                        IssuList.add(v);
                    }
                    books.setBooks(IssuList);
                    books.setCopies(2);
                    IssueBook book1 = issuedBooks.save(books);
                    return ResponseEntity.ok(book1);
                }
                return ResponseEntity.ok("You Have Already Issued 1 Book Now You Can Issue Only 1 Book...");
            }
            return ResponseEntity.ok("You Have Already Issued Two Books...");
        }
        return ResponseEntity.ok("This Book Id Not Available...");
    }

    @PostMapping("/returnBook/{name}")
    public ResponseEntity<?> returnBook(@RequestBody GetBooks books, @PathVariable("name") String name) {
        try {
            int id = 0;
            List<IssueBook> listIssued = this.issuedBooks.findAll().stream().filter(a -> a.getReaderName().equals(name)).collect(Collectors.toList());
            if (listIssued.size() == 0) {
                return ResponseEntity.ok("Data Not Available...");
            }
            boolean as = books11.existsById(books.getBookId());
            if (as) {
                IssueBook issueBook = new IssueBook();
                for (IssueBook a : listIssued) {
                    id = a.getId();
                    issueBook = a;
                }
                LocalDate dateOfIssue = issueBook.getDateOfIssue();
                List<GetBooks> getBooks = issueBook.getBooks();
                long penelty = 0;
                for (GetBooks a : getBooks) {
                    if (a.getBookId() == books.getBookId()) {
                        getBooks.remove(a);
                        if (issueBook.getBooks().size() == 1) {
                            penelty = operation.dateCals(dateOfIssue);
                            issueBook.setBooks(getBooks);
                            issueBook.setCopies(issueBook.getCopies() - 1);
                            issuedBooks.save(issueBook);
                            return ResponseEntity.ok(" Successfully Completed... \n Your Penalty Is $ : " + penelty);
                        } else {
                            issuedBooks.deleteById(id);
                            penelty = operation.dateCals(dateOfIssue);
                            return ResponseEntity.ok(" Successfully Completed... \n Your Penalty Is $ " + penelty);
                        }
                    }
                }
            } else {
                return ResponseEntity.ok("Invalid Book Id ...");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Data Not Available............");
        }
        return ResponseEntity.ok("Data Not Available............");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> showSingle(@PathVariable("name") String Name) {
        List<IssueBook> listIssued = this.issuedBooks.findAll().stream().filter(c -> c.getReaderName().equals(Name)).collect(Collectors.toList());
        if (listIssued.size() == 0) {
            return ResponseEntity.ok("No Data Found For Username...: " + Name);
        }
        return ResponseEntity.ok(listIssued);
    }

    @GetMapping("/issuedata")
    public List<IssueBook> issued() {
        if (issuedBooks.findAll().size() == 0) {
            return null;
        }
        return issuedBooks.findAll();
    }
}