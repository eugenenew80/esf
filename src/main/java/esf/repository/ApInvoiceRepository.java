package esf.repository;

import esf.common.repository.Repository;
import esf.entity.ApInvoice;

public interface ApInvoiceRepository extends Repository<ApInvoice> {
	String toXML(Long invoice_id);
} 
