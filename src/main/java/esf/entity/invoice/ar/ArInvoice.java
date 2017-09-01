package esf.entity.invoice.ar;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import esf.common.entity.HasId;
import esf.entity.Company;
import esf.entity.Customer;
import esf.entity.InvoiceStatus;
import esf.entity.InvoiceType;
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
	private ArDeliveryItem deliveryItem;
	private Set<ArProduct> products;
	private Date turnoverDate;
	private InvoiceStatus status;
	private String currencyCode;
	private Long userId;
	
	
	@Transient
	public Double getTotalNdsAmount() {
		return getProducts().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getNdsAmount()).orElse(0d))
				.sum();
	}
	
	@Transient
	public Double getTotalPriceWithTax() {
		return getProducts().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getPriceWithTax()).orElse(0d))
				.sum();
	}
	
	@Transient
	public Double getTotalPriceWithoutTax() {
		return getProducts().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getPriceWithoutTax()).orElse(0d))
				.sum();
	}	
}
