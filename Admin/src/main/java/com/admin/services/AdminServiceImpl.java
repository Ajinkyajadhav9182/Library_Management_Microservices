package com.admin.services;

import com.admin.entity.GetBooks1;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    public static List<GetBooks1> getBook1s = new ArrayList<>();

    static {
        getBook1s.add(new GetBooks1(1, "marathi", "kale", "pune", 195.1F,  11));
        getBook1s.add(new GetBooks1(2, "hindi", "sharma", "delhi", 295.1F,  12));
        getBook1s.add(new GetBooks1(2, "hindi", "sharma", "delhi", 295.1F, 12));
        getBook1s.add(new GetBooks1(4, "japan", "brusli", "tyoko", 495.1F, 13));
        getBook1s.add(new GetBooks1(3, "english", "roy", "london", 495.1F, 113));
    }

    @Override
    public List<GetBooks1> getallbooksdetaills(int id) {
        return this.getBook1s.stream().filter(a -> a.getBookId() == (id)).collect(Collectors.toList());
    }
}
