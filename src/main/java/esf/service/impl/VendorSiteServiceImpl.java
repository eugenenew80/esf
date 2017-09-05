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
import esf.entity.VendorSite;
import esf.repository.VendorSiteRepository;
import esf.service.VendorSiteService;

@Stateless
public class VendorSiteServiceImpl extends AbstractEntityService<VendorSite> implements VendorSiteService {
    
	@Inject
    public VendorSiteServiceImpl(Repository<VendorSite> repository, Validator validator) {
		super(repository, validator);
    }

	
	@Override
	public VendorSite findByContractNum(Long vendorId, String contractNum) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (vendorId==null || contractNum==null)
			throw new InvalidArgumentException();
				
		VendorSite entity = ((VendorSiteRepository)repository).selectByContractNum(vendorId, contractNum);
		if (entity==null)
			throw new EntityNotFoundException(contractNum);

		return entity;
	}

	
	@Override
	public List<VendorSite> findByVendorId(Long vendorId) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (vendorId==null)
			throw new InvalidArgumentException();
				
		return ((VendorSiteRepository)repository).selectByVendorId(vendorId);
	}	
}