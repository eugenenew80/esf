package esf.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Bank {
	private Long id;
	private String nameRu;
	private String nameKz;
	private String bik;
	private String code;
	private String tin;
	private String rnn;
	private Boolean active;
}
