package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.invoice.ar.ArDeliveryItem;
import esf.service.ArDeliveryItemService;

@Stateless
public class ArDeliveryItemServiceImpl extends AbstractEntityService<ArDeliveryItem> implements ArDeliveryItemService {
    
	@Inject
    public ArDeliveryItemServiceImpl(Repository<ArDeliveryItem> repository, Validator validator) {
		super(repository, validator);
    }	
}
