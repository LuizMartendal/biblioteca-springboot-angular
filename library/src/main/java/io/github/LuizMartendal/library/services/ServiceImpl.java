package io.github.LuizMartendal.library.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public abstract class ServiceImpl<T> implements io.github.LuizMartendal.library.services.Service<T> {

    public abstract JpaRepository<T, UUID> getRepository();

    @Transactional
    @Override
    public T create(T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    @Override
    public List<T> retrieveAll() {
        return getRepository().findAll();
    }

    @Transactional
    @Override
    public T retrieve(UUID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Transactional
    @Override
    public T update(UUID id, T entity) {
        T foundEntity = retrieve(id);
        for (Field foundField : foundEntity.getClass().getDeclaredFields()) {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getName().equals(foundField.getName()) && !"id".equals(field.getName())
                && !"serialVersionUID".equals(field.getName())) {
                    foundField.setAccessible(true);
                    field.setAccessible(true);
                    try {
                        foundField.set(foundEntity, field.get(entity));
                    } catch (Exception e) {
                        throw new RuntimeException("Verify the fields");
                    }
                }
            }
        }
        return null;
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        getRepository().delete(retrieve(id));
    }

    @Transactional
    @Override
    public void delete(T entity) {
        getRepository().delete(entity);
    }
}
