package esf.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import esf.common.entity.HasId;
import esf.common.entity.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(name= "Responsibility.findAll", query="select t from Responsibility t order by t.userId, t.id"),
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class Responsibility implements HasId, HasName {
	private Long id;
	private String name;
	private Long userId;
	private Long orgId;
}
