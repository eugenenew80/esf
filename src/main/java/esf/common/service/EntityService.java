package esf.common.service;

import java.util.List;

import esf.common.entity.HasId;
import esf.common.repository.Repository;
import esf.common.repository.query.Query;

public interface EntityService<T extends HasId> {
	List<T> findAll();
	
	List<T> find(Query query);
	
	T findById(Long entityId);
    
	T findByName(String entityName);
	
	T create(T entity);

	T update(T entity);

    boolean delete(Long entityId);
    
    void setRepository(Repository<T> repository);
}
