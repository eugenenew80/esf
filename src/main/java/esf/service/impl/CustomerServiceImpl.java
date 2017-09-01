package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.Customer;
import esf.repository.CustomerRepository;
import esf.service.CustomerService;

@Stateless
public class CustomerServiceImpl extends AbstractEntityService<Customer> implements CustomerService {

    @Inject
    public CustomerServiceImpl(Repository<Customer> repository, Validator validator) {
    	super(repository, validator);
    }	
    
    
	@Override
	public Customer findByTin(String tin) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (tin==null)
			throw new InvalidArgumentException();
		
		Customer entity = ((CustomerRepository)repository).selectByTin(tin);
		if (entity==null)
			throw new EntityNotFoundException(tin);

		return entity;	
	}
}
