package esf.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class PublicOffice {
	private Long id;
	private String bik;
	private String iik;
	private String payPurpose;
	private String productCode;
}
