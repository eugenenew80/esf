package esf.entity.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SellerDto {
	private Long id;
	private String address;
	private String name;
	private String tin;
	private String rnn;
	private String trailer;	
	private String bank;
	private String bik;
	private String certificateNum;
	private String certificateSeries;
	private Date deliveryDocDate;
	private String deliveryDocNum;
	private String iik;
	private String kbe;
}
