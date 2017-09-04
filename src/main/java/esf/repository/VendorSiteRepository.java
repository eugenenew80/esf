package esf.repository;

import java.util.List;

import esf.common.repository.Repository;
import esf.entity.VendorSite;;

public interface VendorSiteRepository extends Repository<VendorSite> {
	VendorSite selectByContractNum(Long vendorId, String contractNum);
	List<VendorSite> selectByVendorId(Long vendorId);
} 
