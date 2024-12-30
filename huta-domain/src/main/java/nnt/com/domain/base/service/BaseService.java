package nnt.com.domain.base.service;

import org.springframework.data.domain.Page;

public interface BaseService<T, K> {
    T save(T t);

    T update(T t);

    T getById(K id);

    Page<T> getAll(int page, int size, String sort, String direction);

    void delete(K id);
}
