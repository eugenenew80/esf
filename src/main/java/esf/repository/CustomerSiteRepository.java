package esf.repository;

import java.util.List;

import esf.common.repository.Repository;
import esf.entity.CustomerSite;

public interface CustomerSiteRepository extends Repository<CustomerSite> {
	CustomerSite selectByContractNum(Long customerId, String contractNum);
	List<CustomerSite> selectByCustomerId(Long customerId);
} 
