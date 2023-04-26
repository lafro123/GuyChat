package DAO;

import java.util.List;

public interface DAO<T> { // interface DAO qui est utilise par UserDAO et MessageDAO
    T get(int id);
    List<T> getAll();
    void save(T t);
    void update(T t);
    void delete(T t);
}
