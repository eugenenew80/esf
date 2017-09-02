package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import esf.common.repository.AbstractRepository;
import esf.entity.ArDeliveryItem;
import esf.repository.ArDeliveryItemRepository;

@Stateless
public class ArDeliveryItemRepositoryImpl extends AbstractRepository<ArDeliveryItem> implements ArDeliveryItemRepository {
	public ArDeliveryItemRepositoryImpl() { setClazz(ArDeliveryItem.class); }

	public ArDeliveryItemRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
