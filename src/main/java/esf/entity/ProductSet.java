package esf.entity;

import java.util.Set;

import esf.entity.invoice.ar.ArProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of= {"id"})
public class ProductSet {
	private Long id;
	private String currencyCode;
	private Double currencyRate;
	private Double totalExciseAmount;
	private Double totalNdsAmount;
	private Double totalPriceWithTax;
	private Double totalPriceWithoutTax;
	private Double totalTurnoverSize;
	private Set<ArProduct> products;
}
