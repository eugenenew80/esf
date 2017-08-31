package esf.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Taxpayer {
	private Long id;
	private String tin;
	private String nameRu;
	private String firstNameRu;
	private String lastNameRu;
	private String middleNameRu;
	private String nameKz;
	private String firstNameKz;
	private String lastNameKz;
	private String middleNameKz;
	private String addressRu;
	private String addressKz;
	private String certificateSeries;
	private String certificateNum;
	private Boolean resident;
	private Taxpayer headOffice;
	private EnterpriseType enterpriseType;
	private SettlementAccount settlementAccount;
	private String enterpriseAdministrator;
}
