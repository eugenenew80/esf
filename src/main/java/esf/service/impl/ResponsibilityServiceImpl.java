package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.Responsibility;
import esf.service.ResponsibilityService;

@Stateless
public class ResponsibilityServiceImpl extends AbstractEntityService<Responsibility> implements ResponsibilityService {
    
	@Inject
    public ResponsibilityServiceImpl(Repository<Responsibility> repository, Validator validator) {
		super(repository, validator);
    }	
}
