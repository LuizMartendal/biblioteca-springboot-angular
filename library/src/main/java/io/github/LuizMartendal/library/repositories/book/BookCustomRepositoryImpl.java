package io.github.LuizMartendal.library.repositories.book;

import com.querydsl.core.types.dsl.EntityPathBase;
import io.github.LuizMartendal.library.models.entities.Book;
import io.github.LuizMartendal.library.models.entities.QBook;
import io.github.LuizMartendal.library.repositories.RepositoryImpl;

public class BookCustomRepositoryImpl extends RepositoryImpl<Book> implements BookCustomRepository {

    @Override
    public EntityPathBase<Book> getEntity() {
        return QBook.book;
    }
}
