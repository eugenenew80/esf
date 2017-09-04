package esf.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import esf.common.entity.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(name= "VendorSite.findAll", 		  query="select t from VendorSite t"),
	@NamedQuery(name= "VendorSite.findByVendor", 	  query="select t from VendorSite t where t.vendor=:vendor"),
	@NamedQuery(name= "VendorSite.findByContractNum", query="select t from VendorSite t where t.contractNum=:contractNum"),
})
@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class VendorSite implements HasId {
	private Long id;
	
	@NotNull
	private Date contractDate;
	
	@NotNull
	private String contractNum;
	
	@NotNull
	private Vendor vendor;
	
	private String destination;
	private String exerciseWay;
	private String term;	
}
