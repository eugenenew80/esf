package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.CustomerSite;
import esf.service.CustomerSiteService;

@Stateless
public class CustomerSiteServiceImpl extends AbstractEntityService<CustomerSite> implements CustomerSiteService {
    
	@Inject
    public CustomerSiteServiceImpl(Repository<CustomerSite> repository, Validator validator) {
		super(repository, validator);
    }	
}
