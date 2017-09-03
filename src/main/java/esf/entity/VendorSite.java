package esf.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import esf.common.entity.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(name= "VendorSite.findAll", query="select t from VendorSite t"),
})
@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class VendorSite implements HasId {
	private Long id;
	private Date contractDate;
	private String contractNum;
	private String destination;
	private String exerciseWay;
	private String term;
	private Vendor vendor;
}
