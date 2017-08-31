package esf.common.repository;

import java.util.List;

import esf.common.entity.HasId;
import esf.common.repository.query.Query;

public interface Repository <T extends HasId> {
    List<T> selectAll();

    List<T> select(Query query);
    
    T selectById(Long entityId);
    
    T selectByName(String entityName);
    
    T insert(T entity);

    T update(T entity);

    boolean delete(Long entityId);
}
