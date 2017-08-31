package esf.entity;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import esf.common.entity.*;
import lombok.*;

@NamedQueries({
	@NamedQuery(name= "Company.findAll",    query="select t from Company t order by t.name"),
	@NamedQuery(name= "Company.findByName", query="select t from Company t where t.name=:name"),
	@NamedQuery(name= "Company.findByTin",  query="select t from Company t where t.tin=:tin")
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class Company implements HasId, HasName {
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull @Size(min=12, max = 12)
	private String tin;
	
	private String address;
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
