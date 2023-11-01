package io.github.LuizMartendal.library.repositories.person;

import com.querydsl.core.types.dsl.EntityPathBase;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.models.entities.QPerson;
import io.github.LuizMartendal.library.repositories.RepositoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PersonCustomRepositoryImpl extends RepositoryImpl<Person> implements PersonCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntityPathBase<Person> getEntity() {
        return QPerson.person;
    }


}
