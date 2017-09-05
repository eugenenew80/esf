package esf.service;

import java.util.List;
import esf.common.service.EntityService;
import esf.entity.CustomerSite;

public interface CustomerSiteService extends EntityService<CustomerSite> {
	CustomerSite findByContractNum(Long customerId, String contractNum);
	List<CustomerSite> findByCustomerId(Long customerId);
}
