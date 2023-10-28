package io.github.LuizMartendal.library.controllers;

import io.github.LuizMartendal.library.services.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public abstract class ControllerImpl<T> implements Controller<T> {

    public abstract Service<T> getService();

    @PostMapping
    @Override
    public ResponseEntity<T> create(@RequestBody @Valid T entity) {
        return ResponseEntity.ok().body(getService().create(entity));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<T>> retrieveAll() {
        return ResponseEntity.ok().body(getService().retrieveAll());
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<T> retrieve(@PathVariable UUID id) {
        return ResponseEntity.ok().body(getService().retrieve(id));
    }

    @PutMapping("{id}")
    @Override
    public ResponseEntity<T> update(@PathVariable UUID id, @RequestBody @Valid T entity) {
        return ResponseEntity.ok().body(getService().update(id, entity));
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        getService().delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Override
    public ResponseEntity<Void> delete(@RequestBody @Valid T entity) {
        getService().delete(entity);
        return ResponseEntity.ok().build();
    }
}
