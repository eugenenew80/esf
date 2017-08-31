package esf.entity;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Participant {
	private Long id;
	private String rnn;
	private String tin;
	private Set<ProductShare> productShares;
}
