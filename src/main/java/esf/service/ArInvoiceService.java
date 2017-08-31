package esf.service;

import esf.common.service.EntityService;
import esf.entity.invoice.ar.ArInvoice;

public interface ArInvoiceService extends EntityService<ArInvoice> {
	String toXML(Long invoiceId);
	void fromExternalSystem(Long orgId);
	void fromExternalSystem(String orgCode);
}
