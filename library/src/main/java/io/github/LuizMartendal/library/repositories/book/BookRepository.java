package io.github.LuizMartendal.library.repositories.book;

import io.github.LuizMartendal.library.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, BookCustomRepository {
}
