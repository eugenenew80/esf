package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.ApInvoice;
import esf.repository.ApInvoiceRepository;
import esf.service.ApInvoiceService;;


@Stateless
public class ApInvoiceServiceImpl extends AbstractEntityService<ApInvoice> implements ApInvoiceService {

    @Inject
    public ApInvoiceServiceImpl(Repository<ApInvoice> repository) {
        super(repository);
    }
    
	public String toXML(Long invoice_id) {
		return ((ApInvoiceRepository) repository).toXML(invoice_id);
	}	
}
