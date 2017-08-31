package esf.entity;

import java.util.Date;
import esf.common.entity.HasId;
import esf.common.entity.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Seller implements HasId, HasName {
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
	//private Set<SellerType> statuses;
}
