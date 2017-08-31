package esf.service;

import esf.common.service.EntityService;
import esf.entity.Company;

public interface CompanyService extends EntityService<Company> {
	Company findByTin(String tin);	
}
