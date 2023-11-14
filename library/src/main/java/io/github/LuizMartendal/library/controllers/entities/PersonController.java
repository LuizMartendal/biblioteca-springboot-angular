package io.github.LuizMartendal.library.controllers.entities;

import io.github.LuizMartendal.library.controllers.ControllerImpl;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.services.Service;
import io.github.LuizMartendal.library.services.entities.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Person service", description = "This service is responsible for managing users")
@RestController
@RequestMapping(value = "/person")
public class PersonController extends ControllerImpl<Person> {

    @Autowired
    private PersonService service;

    @Override
    public Service<Person> getService() {
        return service;
    }

    @GetMapping("/logged")
    public ResponseEntity<Person> getLoggedUserEntity() {
        return ResponseEntity.ok().body(service.getLoggedUserEntity());
    }

}
