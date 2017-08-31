package esf.repository;

import esf.common.repository.Repository;
import esf.entity.Vendor;

public interface VendorRepository extends Repository<Vendor> {
	Vendor selectByTin(String tin);
} 
