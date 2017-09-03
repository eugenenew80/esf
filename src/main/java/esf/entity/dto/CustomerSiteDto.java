package esf.entity.dto;

import java.util.Date;
import esf.entity.Customer;
import lombok.Data;

@Data
public class CustomerSiteDto {
	private Long id;
	private Date contractDate;
	private String contractNum;
	private String destination;
	private String exerciseWay;
	private String term;
	private Customer customer;
}
