package esf.service;

import esf.common.service.EntityService;
import esf.entity.Customer;

public interface CustomerService extends EntityService<Customer> {
	Customer findByTin(String tin);	
}
