package esf.entity.dto;

import java.util.Date;

import esf.entity.Vendor;
import lombok.Data;

@Data
public class ApDeliveryItemDto {
	private Long id;
	private Date contractDate;
	private String contractNum;
	private String destination;
	private String exerciseWay;
	private String term;
	private Vendor vendor;
}
