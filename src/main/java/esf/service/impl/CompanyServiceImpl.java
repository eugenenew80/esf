package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.Company;
import esf.repository.CompanyRepository;
import esf.service.CompanyService;

@Stateless
public class CompanyServiceImpl extends AbstractEntityService<Company> implements CompanyService {
    
	@Inject
    public CompanyServiceImpl(Repository<Company> repository, Validator validator) {
        super(repository, validator);
    }
    
	
	@Override
	public Company findByTin(String tin) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (tin==null)
			throw new InvalidArgumentException();
		
		Company entity = ((CompanyRepository) repository).selectByTin(tin);
		if (entity==null)
			throw new EntityNotFoundException(tin);

		return entity;	
	}
}
