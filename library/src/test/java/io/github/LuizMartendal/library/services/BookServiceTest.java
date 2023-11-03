package io.github.LuizMartendal.library.services;

import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import io.github.LuizMartendal.library.mocks.MockBook;
import io.github.LuizMartendal.library.models.entities.Book;
import io.github.LuizMartendal.library.repositories.book.BookRepository;
import io.github.LuizMartendal.library.services.entities.BookService;
import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    private BookRepository repository;

    @BeforeEach
    public void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() {
        Page<Book> page = new Page<>();
        page.setContent(input.mockManyCreatedBook(10));
        page.setTotalPages(2L);
        page.setTotalElements(20L);

        when(repository.list(FilterImpl.parse("", 10, 0, "asc"))).thenReturn(page);

        Page<Book> pageResult = service.list(FilterImpl.parse("", 10, 0, "asc"));
        assertNotNull(pageResult);
        assertNotNull(pageResult.getContent());
        assertEquals(20L, pageResult.getTotalElements());
        assertEquals(2L, pageResult.getTotalPages());
    }

    @Test
    public void testGetById() {
        Book book = input.mockCreatedBook(1);
        UUID uuid = UUID.randomUUID();
        book.setId(uuid);

        when(repository.getById(uuid)).thenReturn(book);

        Book bookResult = service.getById(uuid);
        assertNotNull(bookResult);
        assertNotNull(bookResult.getId());
        assertEquals("Not authenticated", bookResult.getCreatedBy());
        assertNotNull(bookResult.getCreatedIn());
        assertEquals("Not authenticated", bookResult.getUpdatedBy());
        assertNotNull(bookResult.getUpdatedIn());
        assertEquals("Title 1", bookResult.getTitle());
        assertEquals("Author 1", bookResult.getAuthor());
        assertEquals(123.0, bookResult.getPrice(), 0.0);
        assertNotNull(bookResult.getLaunchDate());
    }

    @Test
    public void testGetByIdWithNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.getById(uuid)).thenReturn(null);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            service.getById(uuid);
        });

        assertNotNull(exception);
        assertEquals("Entity not found", exception.getMessage());
    }

    @Test
    public void testGetByIdWithNullId() {
        Exception exception = assertThrows(BadRequestException.class, () -> {
            service.getById(null);
        });

        assertNotNull(exception);
        assertEquals("Id cannot be null", exception.getMessage());
    }

    @Test
    public void testCreate() {
        Book book = input.mockBook(1);
        Book persisted = input.mockCreatedBook(1);

        when(repository.save(book)).thenReturn(persisted);

        Book bookResult = service.create(book);
        assertNotNull(bookResult);
        assertNotNull(bookResult.getId());
        assertEquals("Not authenticated", bookResult.getCreatedBy());
        assertNotNull(bookResult.getCreatedIn());
        assertEquals("Not authenticated", bookResult.getUpdatedBy());
        assertNotNull(bookResult.getUpdatedIn());
        assertEquals("Title 1", bookResult.getTitle());
        assertEquals("Author 1", bookResult.getAuthor());
        assertEquals(123.0, bookResult.getPrice(), 0.0);
        assertNotNull(bookResult.getLaunchDate());
    }

    @Test
    public void testUpdate() {
        Book book = input.mockCreatedBook(1);
        Book updatedBook = input.mockCreatedBook(2);
        updatedBook.setId(book.getId());

        assertNotEquals(updatedBook.getTitle(), book.getTitle());
        assertNotEquals(updatedBook.getAuthor(), book.getAuthor());

        when(repository.getById(book.getId())).thenReturn(book);
        when(repository.save(book)).thenReturn(book);

        Book bookResult = service.update(updatedBook.getId(), updatedBook);
        assertNotNull(bookResult);
        assertNotNull(bookResult.getId());
        assertEquals(updatedBook.getCreatedBy(), bookResult.getCreatedBy());
        assertNotNull(bookResult.getCreatedIn());
        assertEquals(updatedBook.getUpdatedBy(), bookResult.getUpdatedBy());
        assertNotNull(bookResult.getUpdatedIn());
        assertEquals(updatedBook.getTitle(), bookResult.getTitle());
        assertEquals(updatedBook.getAuthor(), bookResult.getAuthor());
        assertEquals(updatedBook.getPrice(), bookResult.getPrice());
        assertEquals(updatedBook.getLaunchDate(), bookResult.getLaunchDate());
    }

    @Test
    public void testDelete() {
        Book book = input.mockCreatedBook(1);

        when(repository.getById(book.getId())).thenReturn(book);

        service.delete(book.getId());
    }

}
