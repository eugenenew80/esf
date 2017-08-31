package esf.common.service;

import java.util.*;
import javax.inject.Inject;
import javax.validation.*;
import esf.common.entity.*;
import esf.common.exception.*;
import esf.common.repository.*;
import esf.common.repository.query.Query;

public abstract class AbstractEntityService<T extends HasId> implements EntityService<T> {
	
    public AbstractEntityService(Repository<T> repository) {
        this.repository = repository;
    }
    
    public AbstractEntityService(Repository<T> repository, Validator validator) {
        this(repository);
        this.validator = validator;
    }
    
    
    
	public List<T> findAll() {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		return repository.selectAll();
	}

	
	public List<T> find(Query query) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		return repository.select(query);
	}


	public T findById(Long entityId) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entityId==null)
			throw new InvalidArgumentException();
		
		T entity = repository.selectById(entityId);
		if (entity==null)
			throw new EntityNotFoundException(entityId);
		
		return entity;
	}
	

	public T findByName(String entityName) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (entityName==null)
			throw new InvalidArgumentException();
		
		T entity = repository.selectByName(entityName);
		if (entity==null)
			throw new EntityNotFoundException(entityName);

		return entity;
	}
	
	
	public T create(T entity) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entity==null) 
			throw new InvalidArgumentException();
		
		if (entity.getId()!=null)
			throw new InvalidArgumentException(entity);
			
		if (entity instanceof HasDates) {
			((HasDates) entity).setCreateDate(new Date());
			((HasDates) entity).setUpdateDate(new Date());
		}
		
		Set<ConstraintViolation<T>> violations =  validator.validate(entity);
		if (violations.size()>0) {			
			ConstraintViolation<T> violation = violations.iterator().next();
			throw new ValidationException(violation.getPropertyPath() + ": " + violation.getMessage());
		}
		
		return repository.insert(entity);
	}

	
	public T update(T entity) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entity==null) 
			throw new InvalidArgumentException();
		
		if (entity.getId()==null) 
			throw new InvalidArgumentException(entity);

		T currentEntity = findById(entity.getId());
		if (entity instanceof HasDates) {
			((HasDates) entity).setCreateDate( ((HasDates)currentEntity).getCreateDate() );
			((HasDates) entity).setUpdateDate(new Date());
		}

		Set<ConstraintViolation<T>> violations =  validator.validate(entity);
		if (violations.size()>0) {			
			ConstraintViolation<T> violation = violations.iterator().next();
			throw new ValidationException(violation.getPropertyPath() + ": " + violation.getMessage());
		}
		
		return repository.update(entity);
	}

	
	public boolean delete(Long entityId) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entityId==null) 
			throw new InvalidArgumentException();		
		
		findById(entityId);
		
		return repository.delete(entityId); 
	}	
	
	
	public void setRepository(Repository<T> repository) { 
		this.repository = repository; 
	}	
	
	@Inject protected Repository<T> repository;	
	@Inject protected Validator validator;
}
