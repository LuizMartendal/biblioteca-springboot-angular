package io.github.LuizMartendal.library.services;

import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import io.github.LuizMartendal.library.repositories.Repository;
import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public abstract class ServiceImpl<T> implements io.github.LuizMartendal.library.services.Service<T> {

    public abstract JpaRepository<T, UUID> getRepository();

    @Transactional
    @Override
    public T create(T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    @Override
    public Page<T> list(FilterImpl filter) {
        return ((Repository<T>) getRepository()).list(filter);
    }

    @Transactional
    @Override
    public T getById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Id cannot be null");
        }
        T entity = ((Repository<T>) getRepository()).getById(id);
        if (entity != null) {
            return entity;
        }
        throw new NotFoundException("Entity not found");
    }

    @Transactional
    @Override
    public T update(UUID id, T entity) {
        T foundEntity = getById(id);
        for (Field foundField : foundEntity.getClass().getDeclaredFields()) {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getName().equals(foundField.getName()) && !"id".equals(field.getName())
                && !"serialVersionUID".equals(field.getName())) {
                    foundField.setAccessible(true);
                    field.setAccessible(true);
                    try {
                        foundField.set(foundEntity, field.get(entity));
                    } catch (Exception e) {
                        throw new BadRequestException("Verify the fields");
                    }
                }
            }
        }
        return getRepository().save(foundEntity);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        getRepository().delete(getById(id));
    }

    @Transactional
    @Override
    public void delete(T entity) {
        getRepository().delete(entity);
    }
}
