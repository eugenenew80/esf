package esf.entity.dto;

import java.util.Date;
import lombok.Data;

@Data
public class VendorSiteDto {
	private Long id;
	private Date contractDate;
	private String contractNum;
	private String destination;
	private String exerciseWay;
	private String term;
	private Long vendorId;
}
