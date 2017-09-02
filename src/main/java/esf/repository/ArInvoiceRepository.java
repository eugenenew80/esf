package esf.repository;

import esf.common.repository.Repository;
import esf.entity.ArInvoice;

public interface ArInvoiceRepository extends Repository<ArInvoice> {
	String toXML(Long invoiceId);
	void fromExternalSystem(Long orgId);
	void fromExternalSystem(String orgCode);
} 
