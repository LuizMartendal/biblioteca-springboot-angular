package io.github.LuizMartendal.library.mocks;

import io.github.LuizMartendal.library.models.entities.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MockBook {

    public Book mockCreatedBook(int number) {
        Book book = new Book();

        book.setId(UUID.randomUUID());
        book.setCreatedBy("Not authenticated");
        book.setCreatedIn(new Date());
        book.setUpdatedBy("Not authenticated");
        book.setUpdatedIn(new Date());

        book.setTitle("Title " + number);
        book.setAuthor("Author " + number);
        book.setPrice(123.0);
        book.setLaunchDate(new Date());

        return book;
    }

    public Book mockBook(int number) {
        Book book = new Book();

        book.setTitle("Title " + number);
        book.setAuthor("Author " + number);
        book.setPrice(123.0);
        book.setLaunchDate(new Date());

        return book;
    }

    public List<Book> mockManyBook(int number) {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            bookList.add(mockBook(i));
        }
        return bookList;
    }

    public List<Book> mockManyCreatedBook(int number) {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            bookList.add(mockCreatedBook(i));
        }
        return bookList;
    }
}
