package esf.util;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import esf.service.ArInvoiceService;

@Stateless
public class ImportESF {
	
	@Schedule(minute = "*/5", hour="*")
	public void run() {
		invoiceService.fromExternalSystem("50");
	}
	
	@Inject private ArInvoiceService invoiceService;
}
