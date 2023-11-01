package io.github.LuizMartendal.library.controllers;

import io.github.LuizMartendal.library.services.Service;
import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public abstract class ControllerImpl<T> implements Controller<T> {

    public abstract Service<T> getService();

    @Operation(summary = "This method is responsible for create a entity",
            responses = {
                @ApiResponse(responseCode = "200", description = "When the entity is created"),
                @ApiResponse(responseCode = "400", description = "Check the data"),
                @ApiResponse(responseCode = "404", description = "When something is not found")
            })
    @PostMapping(produces = { "application/json", "application/xml" },
                consumes = { "application/json", "application/xml" })
    @Override
    public ResponseEntity<T> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is the entity that will be created") @RequestBody @Valid T entity) {
        return ResponseEntity.ok().body(getService().create(entity));
    }

    @Operation(summary = "This method is responsible for retrieve all entities",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When the entities is retrieved"),
                    @ApiResponse(responseCode = "400", description = "Check the data"),
                    @ApiResponse(responseCode = "404", description = "When something is not found")
            })
    @GetMapping(produces = { "application/json", "application/xml" })
    @Override
    public ResponseEntity<Page<T>> retrieveAll(
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok().body(getService().list(FilterImpl.parse(filter, size, page, order)));
    }

    @Operation(summary = "This method is responsible for retrieve a entity by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When the entity is retrieved"),
                    @ApiResponse(responseCode = "400", description = "Check the data"),
                    @ApiResponse(responseCode = "404", description = "When the entity is not found")
            })
    @GetMapping(value = "{id}", produces = { "application/json", "application/xml" })
    @Override
    public ResponseEntity<T> retrieve(@PathVariable UUID id) {
        return ResponseEntity.ok().body(getService().getById(id));
    }

    @Operation(summary = "This method is responsible for update a entity by id and entity-selve hehe",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When the entity is updated"),
                    @ApiResponse(responseCode = "400", description = "Check the data"),
                    @ApiResponse(responseCode = "404", description = "When the entity is not found")
            })
    @PutMapping(value = "{id}", produces = { "application/json", "application/xml" },
            consumes = { "application/json", "application/xml" })
    @Override
    public ResponseEntity<T> update(@PathVariable UUID id, @RequestBody @Valid T entity) {
        return ResponseEntity.ok().body(getService().update(id, entity));
    }

    @Operation(summary = "This method is responsible for delete a entity by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When the entity is deleted"),
                    @ApiResponse(responseCode = "400", description = "Check the data"),
                    @ApiResponse(responseCode = "404", description = "When the entity is not found")
            })
    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        getService().delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "This method is responsible for delete a entity by entity-selve hehe",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When the entity is deleted"),
                    @ApiResponse(responseCode = "400", description = "Check the data"),
                    @ApiResponse(responseCode = "404", description = "When the entity is not found")
            })
    @DeleteMapping
    @Override
    public ResponseEntity<Void> delete(@RequestBody @Valid T entity) {
        getService().delete(entity);
        return ResponseEntity.ok().build();
    }
}
