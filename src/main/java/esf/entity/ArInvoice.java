package esf.entity;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import esf.common.entity.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;


@NamedQueries({
	@NamedQuery(name= "ArInvoice.findAll",  query="select t from ArInvoice t order by t.id"),
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class ArInvoice implements HasId {
	private Long id;
	private Date invoiceDate;
	private InvoiceType invoiceType;
	private String num;
	private Customer customer;
	private Customer consignee;
	private Company seller;
	private Company consignor;	
	private CustomerSite deliveryItem;
	private Set<ArInvoiceLine> lines;
	private Date turnoverDate;
	private InvoiceStatus status;
	private String currencyCode;
	private Long userId;
	
	
	@Transient
	public Double getTotalNdsAmount() {
		return getLines().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getNdsAmount()).orElse(0d))
				.sum();
	}
	
	@Transient
	public Double getTotalPriceWithTax() {
		return getLines().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getPriceWithTax()).orElse(0d))
				.sum();
	}
	
	@Transient
	public Double getTotalPriceWithoutTax() {
		return getLines().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getPriceWithoutTax()).orElse(0d))
				.sum();
	}	
}
