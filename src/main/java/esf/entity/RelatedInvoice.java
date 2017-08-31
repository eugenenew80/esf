package esf.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class RelatedInvoice {
	private Long id;
	private Date date;
	private String num;
	private String registrationNumber;
}
