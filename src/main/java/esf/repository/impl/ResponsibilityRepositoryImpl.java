package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import esf.common.repository.AbstractRepository;
import esf.entity.Responsibility;
import esf.repository.ResponsibilityRepository;


@Stateless
public class ResponsibilityRepositoryImpl extends AbstractRepository<Responsibility> implements ResponsibilityRepository {
	public ResponsibilityRepositoryImpl() { setClazz(Responsibility.class); }

	public ResponsibilityRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
