package esf.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import esf.common.repository.Repository;
import esf.common.service.AbstractEntityService;
import esf.entity.ArInvoice;
import esf.repository.ArInvoiceRepository;
import esf.service.ArInvoiceService;;


@Stateless
public class ArInvoiceServiceImpl extends AbstractEntityService<ArInvoice> implements ArInvoiceService {

    @Inject
    public ArInvoiceServiceImpl(Repository<ArInvoice> repository) {
        super(repository);
    }	
	
	public String toXML(Long invoiceId) {
		return ((ArInvoiceRepository) repository).toXML(invoiceId);
	}

	@Override
	public void fromExternalSystem(Long orgId) {
		((ArInvoiceRepository) repository).fromExternalSystem(orgId);
	}

	@Override
	public void fromExternalSystem(String orgCode) {
		((ArInvoiceRepository) repository).fromExternalSystem(orgCode);
	}	
}
