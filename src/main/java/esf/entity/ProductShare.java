package esf.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class ProductShare {
	private Long id;
	private String additional;
	private Double exciseAmount;
	private Double ndsAmount;
	private Double priceWithTax;
	private Double priceWithoutTax;
	private Long productNumber;
	private Double quantity;
	private Double turnoverSize;
}
