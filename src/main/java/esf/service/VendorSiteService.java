package esf.service;

import java.util.List;

import esf.common.service.EntityService;
import esf.entity.VendorSite;

public interface VendorSiteService extends EntityService<VendorSite> {
	VendorSite findByContractNum(Long vendorId, String contractNum);
	List<VendorSite> findByVendorId(Long vendorId);
}
