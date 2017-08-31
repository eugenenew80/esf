package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.Vendor;
import esf.repository.VendorRepository;
import esf.service.VendorService;

@Stateless
public class VendorServiceImpl extends AbstractEntityService<Vendor> implements VendorService {

	@Inject
    public VendorServiceImpl(Repository<Vendor> repository) {
        super(repository);
    }	
	
	@Override
	public Vendor findByTin(String tin) {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		if (tin==null)
			throw new InvalidArgumentException();
		
		Vendor entity = ((VendorRepository)repository).selectByTin(tin);
		if (entity==null)
			throw new EntityNotFoundException(tin);

		return entity;	
	}
}
