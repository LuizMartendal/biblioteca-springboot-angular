package io.github.LuizMartendal.library.services;

import java.util.List;
import java.util.UUID;

public interface Service<T> {

    T create(T entity);

    List<T> retrieveAll();

    T retrieve(UUID id);

    T update(UUID id, T entity);

    void delete(UUID id);

    void delete(T entity);
}
