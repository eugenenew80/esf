package esf.entity;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class User {
	private Long id;
	private String login;
	private String email;
	private Date issueDate;
	private String issueBy;
	private String passportNum;
	private UserStatusType status;
	private String reason;
	private Taxpayer taxpayer;
	private Set<BusinessUser> enterpriseEntries;
}
