package esf.entity;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class ArInvoiceLine {
	private Long id;
	private String description;
	private Double ndsAmount;
	private Double ndsRate;
	private Double priceWithTax;
	private Double priceWithoutTax;
	private Double quantity;
	private Double turnoverSize;
	private String unitNomenclature;
	private Double unitPrice;
	private ArInvoice invoice;
}
