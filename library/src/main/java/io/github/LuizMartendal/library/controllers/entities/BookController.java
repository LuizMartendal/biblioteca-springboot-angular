package io.github.LuizMartendal.library.controllers.entities;

import io.github.LuizMartendal.library.controllers.ControllerImpl;
import io.github.LuizMartendal.library.models.entities.Book;
import io.github.LuizMartendal.library.services.Service;
import io.github.LuizMartendal.library.services.entities.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book service", description = "This service is responsible for managing books")
@RestController
@RequestMapping("/book")
public class BookController extends ControllerImpl<Book> {

    @Autowired
    private BookService service;

    @Override
    public Service<Book> getService() {
        return service;
    }
}
