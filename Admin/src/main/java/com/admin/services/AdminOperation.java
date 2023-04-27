package com.admin.services;

import com.admin.entity.GetSetBooks;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class AdminOperation {
    public GetSetBooks update(GetSetBooks book1, GetSetBooks book2) {
        book1.setBookName(book2.getBookName());
        book1.setAuthorName(book2.getAuthorName());
        book1.setPublication(book2.getPublication());
        book1.setPrice(book2.getPrice());
        book1.setAvailableCopies(book2.getAvailableCopies());
        return book1;
    }

//        public GetSetBooks update(GetSetBooks book, int copies) {
//        book.setAvailableCopies(book.getAvailableCopies() - copies);
//        return book;
//    }
//
//
//
//    public GetSetBooks updateReturn(GetSetBooks book, int copies) {
//        book.setAvailableCopies(book.getAvailableCopies() + copies);
//        return book;
//    }


//        public boolean available(GetSetBooks getSetBooks, IssueBook issueBook) {
//        if (issueBook.getCopies() == 0 && issueBook.getCopies() < 3) {
//            return false;
//        } else if (issueBook.getCopies() <= getSetBooks.getAvailableCopies()) {
//            return true;
//        }
//        return false;
//    }
}