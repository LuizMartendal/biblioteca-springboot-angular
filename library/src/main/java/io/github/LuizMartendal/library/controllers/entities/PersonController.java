package io.github.LuizMartendal.library.controllers.entities;

import io.github.LuizMartendal.library.controllers.ControllerImpl;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.services.Service;
import io.github.LuizMartendal.library.services.entities.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "person")
public class PersonController extends ControllerImpl<Person> {

    @Autowired
    private PersonService service;

    @Override
    public Service<Person> getService() {
        return service;
    }
}
