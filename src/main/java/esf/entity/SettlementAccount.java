package esf.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class SettlementAccount {
	private Long id;
	private String taxpayerTin;
	private Bank bank;
	private Integer accountType;
	private String account;
	private Date dateOpen;
	private Date dateClose;
}
