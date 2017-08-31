package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import esf.common.repository.AbstractRepository;
import esf.entity.Vendor;
import esf.repository.VendorRepository;


@Stateless
public class VendorRepositoryImpl extends AbstractRepository<Vendor> implements VendorRepository {
	public VendorRepositoryImpl() { setClazz(Vendor.class); }

	public VendorRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	
	@Override
	public Vendor selectByTin(String tin) {
		TypedQuery<Vendor> query = getEntityManager().createNamedQuery(Vendor.class.getSimpleName() + ".findByTin", Vendor.class);
		query.setParameter("tin", tin);
		
		return query.getResultList().stream()
				.findFirst()
				.orElse(null);
	}	
}
