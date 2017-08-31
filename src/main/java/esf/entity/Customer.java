package esf.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import esf.common.entity.HasId;
import esf.common.entity.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(name= "Customer.findAll",    query="select t from Customer t order by t.name"),
	@NamedQuery(name= "Customer.findByName", query="select t from Customer t where t.name=:name"),
	@NamedQuery(name= "Customer.findByTin",  query="select t from Customer t where t.tin=:tin")
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class Customer implements HasId, HasName {
	private Long id;
	private String address;
	private String name;
	private String rnn;
	private CustomerType status;
	private String tin;
	private String countryCode;
	private String trailer;
}
