package esf.entity.dto;

import java.util.Date;
import java.util.Set;
import esf.entity.Company;
import esf.entity.InvoiceStatus;
import esf.entity.InvoiceType;
import esf.entity.Vendor;
import esf.entity.invoice.ap.ApDeliveryItem;
import esf.entity.invoice.ap.ApProduct;

import lombok.Data;

@Data
public class ApInvoiceDto {
	
	private Long id;
	private Date invoiceDate;
	private InvoiceType invoiceType;
	private String num;
	private Company customer;
	private Company consignee;
	private Vendor seller;
	private Vendor consignor;
	private ApDeliveryItem deliveryItem;
	private Set<ApProduct> products;
	private Date turnoverDate;
	private InvoiceStatus status;
	private String currencyCode;
	private String operatorFullName;	
	
	private Double totalNdsAmount;
	private Double totalPriceWithTax;
	private Double totalPriceWithoutTax;	
	
	private Long consigneeId;
	private String consigneeName;

	private Long customerId;
	private String customerName;

	private Long deliveryItemId;
	private String contractNum;
	private Date contractDate;
	private String destination;
}
