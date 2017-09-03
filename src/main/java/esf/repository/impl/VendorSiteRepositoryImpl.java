package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import esf.common.repository.AbstractRepository;
import esf.entity.VendorSite;
import esf.repository.VendorSiteRepository;

@Stateless
public class VendorSiteRepositoryImpl extends AbstractRepository<VendorSite> implements VendorSiteRepository {
	public VendorSiteRepositoryImpl() { setClazz(VendorSite.class); }

	public VendorSiteRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
