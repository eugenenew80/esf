package esf.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public abstract class AbstractInvoice {
	private Long id;
	private Date date;
	private InvoiceType invoiceType;
	private String num;
	private String operatorFullName;
	private RelatedInvoice relatedInvoice;
	private String signature;
	private SignatureType signatureType;
}
