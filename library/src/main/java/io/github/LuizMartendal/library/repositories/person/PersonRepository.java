package io.github.LuizMartendal.library.repositories.person;

import io.github.LuizMartendal.library.models.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID>, PersonCustomRepository {

    Optional<Person> findByUsername(String username);
}
