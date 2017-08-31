package esf.entity.dto;

import esf.entity.CustomerType;
import lombok.Data;

@Data
public class VendorDto {
	private Long id;
	private String address;
	private String name;
	private String rnn;
	private CustomerType status;
	private String tin;
	private String countryCode;
	private String trailer;
}
