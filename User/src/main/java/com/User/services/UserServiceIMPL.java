package com.User.services;

import com.User.entity.IssueBook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserServiceIMPL implements UserService{
  public static  List<IssueBook>issueBooks=new ArrayList<>();
    static {
        issueBooks.add(new IssueBook(1,11,"ajinkya","pune",785565598L,2));
        issueBooks.add(new IssueBook(2,12,"ajay","mumbai",7852225598L,1));
        issueBooks.add(new IssueBook(3,13,"aditya","khed",78556545458L,2));
    }
    @Override
    public IssueBook getBookid(int id) {
        return this.issueBooks.stream().filter(a->a.getBookId()==(id)).findAny().orElse(null);
    }
}