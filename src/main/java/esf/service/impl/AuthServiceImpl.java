package esf.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import esf.common.repository.query.ConditionType;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.Responsibility;
import esf.service.AuthService;
import esf.service.ResponsibilityService;

@Stateless
public class AuthServiceImpl implements AuthService {

	@Override
	public Long auth(String userName, String password) {
		
        Object userId =  em.createNativeQuery("SELECT xx_esf_pkg.verify_user(?1, ?2) FROM DUAL")
                .setParameter(1, userName)
                .setParameter(2, password)
                .getSingleResult(); 
        
		return Long.parseLong(userId.toString());
	}
	

	@Override
	public List<Responsibility> getResponsibility(Long userId) {
		Query query = QueryImpl.builder()
				.setParameter("userId", new esf.common.repository.query.MyQueryParam("userId", userId, ConditionType.EQUALS))
				.setOrderBy("t.orgId")
				.build();			
		
		return responsibilityService.find(query);
	}
	
	
	@PersistenceContext(unitName = "dev")
	private EntityManager em;	
	
	@Inject 
	private ResponsibilityService responsibilityService;
}
