package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.VendorSite;
import esf.service.VendorSiteService;

@Stateless
public class VendorSiteServiceImpl extends AbstractEntityService<VendorSite> implements VendorSiteService {
    
	@Inject
    public VendorSiteServiceImpl(Repository<VendorSite> repository, Validator validator) {
		super(repository, validator);
    }	
}