package interfaces;

import java.util.List;

public interface Repository<T, ID> {
    boolean save(T object);
    T findById(ID id);
    List<T> findAll();
    boolean update(T object);
    boolean deleteById(ID id);
}
