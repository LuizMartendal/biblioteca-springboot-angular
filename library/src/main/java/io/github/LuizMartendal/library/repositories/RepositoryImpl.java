package io.github.LuizMartendal.library.repositories;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.FilterQueryImpl;
import io.github.LuizMartendal.library.utils.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public abstract class RepositoryImpl<T> implements Repository<T> {

    @PersistenceContext
    protected EntityManager em;

    public abstract EntityPathBase<T> getEntity();

    @Override
    public Page<T> list(FilterImpl filter) {
        JPAQueryBase base = new JPAQuery(em).from(getEntity());

        try {
            for (FilterQueryImpl filterQuery: filter.getFilter()) {
                if (filterQuery.getBooleanValue() != null) {
                    BooleanPath booleanPath = (BooleanPath) getEntity().getClass().getField(filterQuery.getField()).get(getEntity());
                    base.where(booleanPath.eq(filterQuery.getBooleanValue()));
                }
                if (filterQuery.getLongValue() != null) {
                    NumberPath<Long> longNumberPath = (NumberPath<Long>) getEntity().getClass().getField(filterQuery.getField()).get(getEntity());
                    base.where(longNumberPath.eq(filterQuery.getLongValue()));
                    switch (filterQuery.getType()) {
                        case IGUAL: base.where(longNumberPath.eq(filterQuery.getLongValue())); break;
                        case MAIOR:  base.where(longNumberPath.gt(filterQuery.getLongValue())); break;
                        case MAIOR_IGUAL: base.where(longNumberPath.goe(filterQuery.getLongValue())); break;
                        case MENOR: base.where(longNumberPath.lt(filterQuery.getLongValue())); break;
                        case MENOR_IGUAL: base.where(longNumberPath.loe(filterQuery.getLongValue())); break;
                        default: throw new BadRequestException("Incorrect modifier");
                    }
                }
                if (filterQuery.getDateValue() != null) {
                    DateTimePath<Date> dateTimePath = (DateTimePath<Date>) getEntity().getClass().getField(filterQuery.getField()).get(getEntity());
                    switch (filterQuery.getType()) {
                        case IGUAL: base.where(dateTimePath.eq(filterQuery.getDateValue())); break;
                        case MAIOR: base.where(dateTimePath.gt(filterQuery.getDateValue())); break;
                        case  MAIOR_IGUAL: base.where(dateTimePath.goe(filterQuery.getDateValue())); break;
                        case MENOR: base.where(dateTimePath.lt(filterQuery.getDateValue())); break;
                        case MENOR_IGUAL: base.where(dateTimePath.loe(filterQuery.getDateValue())); break;
                        default: throw new BadRequestException("Incorrect modifier");
                    }
                }
                if (filterQuery.getStringValue() != null) {
                    try {
                        UUID uuid = UUID.fromString(filterQuery.getStringValue());
                        ComparablePath<UUID> comparablePath = null;
                        if (filterQuery.getField().contains(".")) {
                            String[] subentityFilter = filterQuery.getField().split("\\.");
                            Object subentity = getEntity().getClass().getField(subentityFilter[0]).get(getEntity());
                            comparablePath = (ComparablePath<UUID>) subentity.getClass().getField(subentityFilter[1]).get(subentity);
                        } else {
                            comparablePath = (ComparablePath<UUID>) getEntity().getClass().getField(filterQuery.getField()).get(getEntity());
                        }
                        base.where(comparablePath.eq(uuid));
                    } catch (Exception e) {
                        StringPath stringPath = (StringPath) getEntity().getClass().getField(filterQuery.getField()).get(getEntity());
                        switch (filterQuery.getType()) {
                            case IGUAL: base.where(stringPath.eq(filterQuery.getStringValue())); break;
                            case MAIOR: base.where(stringPath.gt(filterQuery.getStringValue())); break;
                            case  MAIOR_IGUAL: base.where(stringPath.goe(filterQuery.getStringValue())); break;
                            case MENOR: base.where(stringPath.lt(filterQuery.getStringValue())); break;
                            case MENOR_IGUAL: base.where(stringPath.loe(filterQuery.getStringValue())); break;
                            case LIKE: base.where(stringPath.containsIgnoreCase(filterQuery.getStringValue())); break;
                            default: throw new BadRequestException("Incorrect modifier");
                        }
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            throw new BadRequestException("Verify filters and try again.");
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        try {
            if (filter.getOrder() != null && !filter.getOrder().isEmpty()) {
                String[] order = filter.getOrder().split("\\s+");
                Field field = getEntity().getClass().getField(order[0]);
                if (field.getType().equals(DateTimePath.class)) {
                    DateTimePath<Date> value = (DateTimePath<Date>) field.get(getEntity());
                    if (order[1].equalsIgnoreCase("asc")) {
                        base.orderBy(value.asc());
                    } else {
                        base.orderBy(value.desc());
                    }
                }
                if (field.getType().equals(StringPath.class)) {
                    StringPath value = (StringPath) field.get(getEntity());
                    if (order[1].equalsIgnoreCase("asc")) {
                        base.orderBy(value.asc());
                    } else {
                        base.orderBy(value.desc());
                    }
                }
                if (field.getType().equals(NumberPath.class)) {
                    NumberPath<Long> value = (NumberPath<Long>) field.get(getEntity());
                    if (order[1].equalsIgnoreCase("asc")) {
                        base.orderBy(value.asc());
                    } else {
                        base.orderBy(value.desc());
                    }
                }
                if (field.getType().equals(BooleanPath.class)) {
                    BooleanPath value = (BooleanPath) field.get(getEntity());
                    if (order[1].equalsIgnoreCase("asc")) {
                        base.orderBy(value.asc());
                    } else {
                        base.orderBy(value.desc());
                    }
                }
            } else {
                NumberPath<Long> id = (NumberPath) getEntity().getClass().getField("id").get(getEntity());
                base.orderBy(id.desc());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (filter.getSize() != null && filter.getSize() > 0) {
            base.limit(filter.getSize());
        }
        if (filter.getPage() != null && filter.getPage() > 0) {
            base.offset(filter.getPage());
        }

        Long quantity = base.fetchCount();

        List<T> entity = base.fetch();
        return new Page<>(entity, quantity, filter.getSize() != null ? quantity / filter.getSize() + 1 : 1L);
    }

    @Override
    public T getById(UUID id) {
        JPAQuery<T> base = new JPAQuery<T>(em).from(getEntity());
        try {
            ComparablePath<UUID> comparablePath = (ComparablePath<UUID>) getEntity().getClass().getField("id").get(getEntity());
            base.where(comparablePath.eq(id));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return base.fetch().get(0);
    }
}
