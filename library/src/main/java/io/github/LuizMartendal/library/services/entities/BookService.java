package io.github.LuizMartendal.library.services.entities;

import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.models.entities.Book;
import io.github.LuizMartendal.library.repositories.book.BookRepository;
import io.github.LuizMartendal.library.services.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookService extends ServiceImpl<Book> {

    @Autowired
    private BookRepository repository;

    @Override
    public JpaRepository<Book, UUID> getRepository() {
        return repository;
    }

    @Transactional
    @Override
    public Book update(UUID id, Book entity) {
        String user = PersonService.getLoggedUser();
        Book entityFound = getById(id);
        if (!entityFound.getCreatedBy().equals(user)) {
            throw new BadRequestException("You cannot edit a book that it´s not yours");
        }
        return super.update(id, entity);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        String user = PersonService.getLoggedUser();
        Book entity = getById(id);
        if (!entity.getCreatedBy().equals(user)) {
            throw new BadRequestException("You cannot delete a book that it´s not yours");
        }
        super.delete(id);
    }
}
