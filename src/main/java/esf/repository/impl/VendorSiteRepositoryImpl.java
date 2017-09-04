package esf.repository.impl;

import java.util.List;
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

	
	@Override
	public VendorSite selectByName(String entityName) {
		throw new UnsupportedOperationException("Repository doesn't support this method");
	}

	
	@Override
	public VendorSite selectByContractNum(Long vendorId, String contractNum) {
		return getEntityManager().createNamedQuery(VendorSite.class.getSimpleName() + ".findByContractNum", VendorSite.class)
				.setParameter("vendorId", vendorId)
				.setParameter("contractNum", contractNum)
				.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}

	
	@Override
	public List<VendorSite> selectByVendorId(Long vendorId) {
		return getEntityManager().createNamedQuery(VendorSite.class.getSimpleName() + ".findByVendorId", VendorSite.class)
				.setParameter("vendorId", vendorId)
				.getResultList();
	}	
}
