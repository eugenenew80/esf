package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import esf.common.repository.AbstractRepository;
import esf.entity.ApInvoice;
import esf.repository.ApInvoiceRepository;


@Stateless
public class ApInvoiceRepositoryImpl extends AbstractRepository<ApInvoice> implements ApInvoiceRepository {
	public ApInvoiceRepositoryImpl() { setClazz(ApInvoice.class); }

	public ApInvoiceRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	
	public ApInvoice selectByName(String entityName) {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public String toXML(Long invoice_id) {
        Object xml =  getEntityManager().createNativeQuery("SELECT dbms_lob.substr(xx_esf_pkg.ap_invoice_to_xml(?1), 4000, 1) FROM DUAL")
              .setParameter(1, invoice_id)
              .getSingleResult(); 
        
        return xml.toString();	
    }	
}
