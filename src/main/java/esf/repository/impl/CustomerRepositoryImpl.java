package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import esf.common.repository.AbstractRepository;
import esf.entity.Customer;
import esf.repository.CustomerRepository;


@Stateless
public class CustomerRepositoryImpl extends AbstractRepository<Customer> implements CustomerRepository {
	public CustomerRepositoryImpl() { setClazz(Customer.class); }

	public CustomerRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	
	@Override
	public Customer selectByTin(String tin) {
		TypedQuery<Customer> query = getEntityManager().createNamedQuery(Customer.class.getSimpleName() + ".findByTin", Customer.class);
		query.setParameter("tin", tin);
		
		return query.getResultList().stream()
				.findFirst()
				.orElse(null);
	}	
}
