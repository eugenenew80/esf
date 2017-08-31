package esf.entity.dto;

import lombok.Data;

@Data
public class ResponsibilityDto {
	private Long id;
	private String name;
	private Long userId;
	private Long orgId;
}
