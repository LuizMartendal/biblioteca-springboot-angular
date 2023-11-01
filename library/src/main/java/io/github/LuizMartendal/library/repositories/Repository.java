package io.github.LuizMartendal.library.repositories;

import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository<T> {

    Page<T> retrieveAll(FilterImpl filter);

    T retrieve(UUID id);
}
