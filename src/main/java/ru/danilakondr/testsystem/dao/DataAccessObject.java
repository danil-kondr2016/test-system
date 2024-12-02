package ru.danilakondr.testsystem.dao;

public interface DataAccessObject<T, K> {
    void add(T object);
    void delete(T object);
    T get(K objKey);
    void update(T object);
}
