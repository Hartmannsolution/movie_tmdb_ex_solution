package dk.cphbusiness.moviedb_solution.persistence.daos;

import java.util.Set;

public interface IDAO<T> {
    T create(T t);
    T getById(int id);
    Set<T> getAll();
    T update(T t);
    boolean delete(T t);
}
