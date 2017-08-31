package esf.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import esf.common.entity.HasId;
import esf.common.entity.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(name= "Vendor.findAll",    query="select t from Vendor t order by t.name"),
	@NamedQuery(name= "Vendor.findByName", query="select t from Vendor t where t.name=:name"),
	@NamedQuery(name= "Vendor.findByTin",  query="select t from Vendor t where t.tin=:tin")
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class Vendor implements HasId, HasName {
	private Long id;
	private String address;
	private String name;
	private String rnn;
	private CustomerType status;
	private String tin;
	private String countryCode;
	private String trailer;
}
