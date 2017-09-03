package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import esf.common.repository.AbstractRepository;
import esf.entity.CustomerSite;
import esf.repository.CustomerSiteRepository;

@Stateless
public class CustomerSiteRepositoryImpl extends AbstractRepository<CustomerSite> implements CustomerSiteRepository {
	public CustomerSiteRepositoryImpl() { setClazz(CustomerSite.class); }

	public CustomerSiteRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	@Override
	public CustomerSite selectByName(String entityName) {
		throw new UnsupportedOperationException("Repository doesn't support this method");
	}
	
	
}
