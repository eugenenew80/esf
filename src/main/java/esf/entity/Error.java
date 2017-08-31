package esf.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Error {
	private Long id;
	private String property;
	private String text;
}
