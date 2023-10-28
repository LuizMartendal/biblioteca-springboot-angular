package io.github.LuizMartendal.library.services.entities;

import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.repositories.PersonRepository;
import io.github.LuizMartendal.library.services.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService extends ServiceImpl<Person> {

    @Autowired
    private PersonRepository repository;

    @Override
    public JpaRepository<Person, UUID> getRepository() {
        return repository;
    }
}
