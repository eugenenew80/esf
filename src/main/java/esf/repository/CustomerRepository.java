package esf.repository;

import esf.common.repository.Repository;
import esf.entity.Customer;

public interface CustomerRepository extends Repository<Customer> {
	Customer selectByTin(String tin);
} 
