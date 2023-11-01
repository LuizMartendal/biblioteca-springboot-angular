package io.github.LuizMartendal.library.repositories;

import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository<T> {

    @Transactional
    Page<T> list(FilterImpl filter);

    @Transactional
    T getById(UUID id);
}
