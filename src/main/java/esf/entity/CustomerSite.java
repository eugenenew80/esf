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
	@NamedQuery(name= "CustomerSite.findAll", 		    query="select t from CustomerSite t"),
	@NamedQuery(name= "CustomerSite.findByCustomerId", 	query="select t from CustomerSite t where t.customer.id=:customerId"),
	@NamedQuery(name= "CustomerSite.findByContractNum", query="select t from CustomerSite t where t.customer.id=:customerId and t.contractNum=:contractNum"),	
})
@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class CustomerSite implements HasId {
	private Long id;
	
	@NotNull
	private Date contractDate;
	
	@NotNull
	private String contractNum;
		
	@NotNull
	private Customer customer;
	
	private String destination;
	private String exerciseWay;
	private String term;
}
