package esf.entity.dto;

import java.util.Date;
import esf.entity.Customer;
import esf.entity.InvoiceStatus;
import esf.entity.InvoiceType;
import esf.entity.Seller;
import esf.entity.invoice.ar.ArDeliveryItem;
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
	
	private Seller consignor;
	
	private Customer customer;
	private Long customerId;
	private String customerName;
	
	private Seller seller;
	
	private ArDeliveryItem deliveryItem;
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
