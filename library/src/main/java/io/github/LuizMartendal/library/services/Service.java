package io.github.LuizMartendal.library.services;

import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;

import java.util.List;
import java.util.UUID;

public interface Service<T> {

    T create(T entity);

    Page<T> list(FilterImpl filter);

    T getById(UUID id);

    T update(UUID id, T entity);

    void delete(UUID id);

    void delete(T entity);
}
