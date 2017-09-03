package esf.entity.dto;

import java.util.Date;
import esf.entity.CustomerSite;
import esf.entity.Company;
import esf.entity.Customer;
import esf.entity.InvoiceStatus;
import esf.entity.InvoiceType;
import lombok.Data;

@Data
public class ArInvoiceDto {
	private Long id;
	private Date invoiceDate;
	private InvoiceType invoiceType;
	private String num;
	
	private Customer consignee; 
	private Long consigneeId;
	private String consigneeName;
	
	private Company consignor;
	
	private Customer customer;
	private Long customerId;
	private String customerName;
	
	private Company seller;
	
	private CustomerSite deliveryItem;
	private Long deliveryItemId;
	private String contractNum;
	private Date contractDate;
	private String destination;
	private String currencyCode;
	
	private Date turnoverDate;
	private InvoiceStatus status;
	
	private Double totalNdsAmount;
	private Double totalPriceWithTax;
	private Double totalPriceWithoutTax;
}
