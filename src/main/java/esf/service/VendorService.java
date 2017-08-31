package esf.service;

import esf.common.service.EntityService;
import esf.entity.Vendor;

public interface VendorService extends EntityService<Vendor> {
	Vendor findByTin(String tin);	
}
