package esf.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import esf.common.repository.AbstractRepository;
import esf.entity.ArInvoice;
import esf.repository.ArInvoiceRepository;


@Stateless
public class ArInvoiceRepositoryImpl extends AbstractRepository<ArInvoice> implements ArInvoiceRepository {
	public ArInvoiceRepositoryImpl() { setClazz(ArInvoice.class); }

	public ArInvoiceRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	
	public ArInvoice selectByName(String entityName) {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public String toXML(Long invoiceId) {
        Object xml =  getEntityManager().createNativeQuery("SELECT dbms_lob.substr(xx_esf_pkg.ar_invoice_to_xml(?1), 4000, 1) FROM DUAL")
              .setParameter(1, invoiceId)
              .getSingleResult(); 
        
        return xml.toString();	
    }

	
	@Override
	public void fromExternalSystem(Long orgId) {
		getEntityManager().createNativeQuery("call xx_esf_pkg.import_esf_by_org_id(?1)")
			.setParameter(1, orgId)
			.executeUpdate();
	}	
	
	
	@Override
	public void fromExternalSystem(String orgCode) {
		getEntityManager().createNativeQuery("call xx_esf_pkg.import_esf_by_org_code(?1)")
			.setParameter(1, orgCode)
			.executeUpdate();
	}		
}
