package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.invoice.ap.ApDeliveryItem;
import esf.service.ApDeliveryItemService;

@Stateless
public class ApDeliveryItemServiceImpl extends AbstractEntityService<ApDeliveryItem> implements ApDeliveryItemService {
    
	@Inject
    public ApDeliveryItemServiceImpl(Repository<ApDeliveryItem> repository, Validator validator) {
		super(repository, validator);
    }	
}