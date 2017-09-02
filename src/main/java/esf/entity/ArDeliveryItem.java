package esf.entity;

import java.util.Date;
import javax.persistence.Entity;

import esf.common.entity.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class ArDeliveryItem implements HasId {
	private Long id;
	private Date contractDate;
	private String contractNum;
	private String destination;
	private String exerciseWay;
	private String term;
	private Customer customer;
}
