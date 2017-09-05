package esf.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.CustomerSite;
import esf.repository.CustomerSiteRepository;
import esf.service.CustomerSiteService;

@Stateless
public class CustomerSiteServiceImpl extends AbstractEntityService<CustomerSite> implements CustomerSiteService {
    
	@Inject
    public CustomerSiteServiceImpl(Repository<CustomerSite> repository, Validator validator) {
		super(repository, validator);
    }

	@Override
	public CustomerSite findByContractNum(Long customerId, String contractNum) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (customerId==null || contractNum==null)
			throw new InvalidArgumentException();
				
		CustomerSite entity = ((CustomerSiteRepository)repository).selectByContractNum(customerId, contractNum);
		if (entity==null)
			throw new EntityNotFoundException(contractNum);

		return entity;
	}

	@Override
	public List<CustomerSite> findByCustomerId(Long customerId) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (customerId==null)
			throw new InvalidArgumentException();
				
		return ((CustomerSiteRepository)repository).selectByCustomerId(customerId);
	}	
}
