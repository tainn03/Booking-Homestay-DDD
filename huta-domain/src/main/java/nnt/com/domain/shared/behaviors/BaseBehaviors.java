package nnt.com.domain.shared.behaviors;

import org.springframework.data.domain.Page;

public interface BaseBehaviors<T, K> {
    T save(T t);

    T update(T t);

    T getById(K id);

    Page<T> getAll(int page, int size, String sort, String direction);

    void delete(K id);
}
