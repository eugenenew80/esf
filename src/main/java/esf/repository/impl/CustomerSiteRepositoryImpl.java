package esf.repository.impl;

import java.util.List;

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

	
	@Override
	public CustomerSite selectByContractNum(Long customerId, String contractNum) {
		return getEntityManager().createNamedQuery(CustomerSite.class.getSimpleName() + ".findByContractNum", CustomerSite.class)
				.setParameter("customerId", customerId)
				.setParameter("contractNum", contractNum)
				.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}

	
	@Override
	public List<CustomerSite> selectByCustomerId(Long customerId) {
		return getEntityManager().createNamedQuery(CustomerSite.class.getSimpleName() + ".findByCustomerId", CustomerSite.class)
				.setParameter("customerId", customerId)
				.getResultList();
	}	
}
