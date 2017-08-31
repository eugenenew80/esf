package esf.repository;

import esf.common.repository.Repository;
import esf.entity.Company;

public interface CompanyRepository extends Repository<Company> {
	Company selectByTin(String tin);
} 
