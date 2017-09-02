package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import esf.common.repository.AbstractRepository;
import esf.entity.ApDeliveryItem;
import esf.repository.ApDeliveryItemRepository;

@Stateless
public class ApDeliveryItemRepositoryImpl extends AbstractRepository<ApDeliveryItem> implements ApDeliveryItemRepository {
	public ApDeliveryItemRepositoryImpl() { setClazz(ApDeliveryItem.class); }

	public ApDeliveryItemRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
