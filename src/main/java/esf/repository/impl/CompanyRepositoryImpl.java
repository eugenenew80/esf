package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import esf.common.repository.AbstractRepository;
import esf.entity.Company;
import esf.repository.CompanyRepository;


@Stateless
public class CompanyRepositoryImpl extends AbstractRepository<Company> implements CompanyRepository {
	public CompanyRepositoryImpl() { setClazz(Company.class); }

	public CompanyRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	
	@Override
	public Company selectByTin(String tin) {
		TypedQuery<Company> query = getEntityManager().createNamedQuery(Company.class.getSimpleName() + ".findByTin", Company.class);
		query.setParameter("tin", tin);
		
		return query.getResultList().stream()
				.findFirst()
				.orElse(null);
	}	
}
