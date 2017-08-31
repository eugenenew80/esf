package esf.service;

import esf.common.service.EntityService;
import esf.entity.invoice.ap.ApInvoice;

public interface ApInvoiceService extends EntityService<ApInvoice> {
	String toXML(Long invoice_id);
}
