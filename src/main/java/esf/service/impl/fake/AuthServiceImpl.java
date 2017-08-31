package esf.service.impl.fake;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import esf.entity.Responsibility;
import esf.service.AuthService;

@Stateless
@Alternative
public class AuthServiceImpl implements AuthService {

	@Override
	public Long auth(String userName, String password) {
		return 1762L;
	}	

	@Override
	public List<Responsibility> getResponsibility(Long userId) {		
		List<Responsibility> list = new ArrayList<>();
		
		Responsibility r = new Responsibility();
		r.setId(1L);
		r.setName("01 Дебиторы");
		r.setOrgId(102L);
		r.setUserId(1762L);
		list.add(r);
		
		return list;
	}
}
