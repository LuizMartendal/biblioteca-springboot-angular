package io.github.LuizMartendal.library.controllers;

import io.github.LuizMartendal.library.utils.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface Controller<T> {

    ResponseEntity<T> create(T entity);

    ResponseEntity<Page<T>> retrieveAll(String filter, Integer size, Integer page, String order);

    ResponseEntity<T> retrieve(UUID id);

    ResponseEntity<T> update(UUID id, T entity);

    ResponseEntity<Void> delete(UUID id);

    ResponseEntity<Void> delete(T entity);
}
