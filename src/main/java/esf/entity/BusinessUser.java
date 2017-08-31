package esf.entity;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class BusinessUser {
	private Long id;
	private String iin;
	private String tin;
	private BusinessProfileType businessProfileType;
	private String actsOnTheBasis;
	private UserStatusType status;
	private String reason;
	private Date expirationDate;
	private Date updateDate;
	private Taxpayer enterpriseTaxpayerInfo;
	private Set<Permission> permissions;
	private Set<BusinessUser> branches; 
}
