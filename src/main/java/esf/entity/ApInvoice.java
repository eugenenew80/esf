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
	@NamedQuery(name= "ApInvoice.findAll",  query="select t from ApInvoice t order by t.id"),
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class ApInvoice implements HasId {
	private Long id;
	private Date invoiceDate;
	private InvoiceType invoiceType;
	private String num;
	private Company customer;
	private Company consignee;
	private Vendor seller;
	private Vendor consignor;
	private VendorSite deliveryItem;
	private Set<ApProduct> products;
	private Date turnoverDate;
	private InvoiceStatus status;
	private String currencyCode;
	private String operatorFullName;

	
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
