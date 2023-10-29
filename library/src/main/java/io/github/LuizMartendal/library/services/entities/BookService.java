package io.github.LuizMartendal.library.services.entities;

import io.github.LuizMartendal.library.models.entities.Book;
import io.github.LuizMartendal.library.repositories.BookRepository;
import io.github.LuizMartendal.library.services.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookService extends ServiceImpl<Book> {

    @Autowired
    private BookRepository repository;

    @Override
    public JpaRepository<Book, UUID> getRepository() {
        return repository;
    }
}
