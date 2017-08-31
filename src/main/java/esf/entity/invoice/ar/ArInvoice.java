package esf.entity.invoice.ar;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import esf.common.entity.HasId;
import esf.entity.Company;
import esf.entity.Customer;
import esf.entity.InvoiceStatus;
import esf.entity.InvoiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;


@NamedQueries({
	@NamedQuery(name= "ArInvoice.findAll",  query="select t from ArInvoice t order by t.id"),
})

@Entity
@Data
@EqualsAndHashCode(of= {"id"})
public class ArInvoice implements HasId {
	private Long id;
	private Date invoiceDate;
	private InvoiceType invoiceType;
	private String num;
	private Customer customer;
	private Customer consignee;
	private Company seller;
	private Company consignor;	
	private ArDeliveryItem deliveryItem;
	private Set<ArProduct> products;
	private Date turnoverDate;
	private InvoiceStatus status;
	private String currencyCode;
	private Long userId;
	
	
	@Transient
	public Double getTotalNdsAmount() {
		return getProducts().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getNdsAmount()).orElse(0d))
				.sum();
	}
	
	@Transient
	public Double getTotalPriceWithTax() {
		return getProducts().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getPriceWithTax()).orElse(0d))
				.sum();
	}
	
	@Transient
	public Double getTotalPriceWithoutTax() {
		return getProducts().stream()
				.mapToDouble(d -> Optional.ofNullable(d.getPriceWithoutTax()).orElse(0d))
				.sum();
	}
	
	
	/*
	public String toXML(String operatorFullName) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<v1:invoice>");
		
		//date
		sb.append("<date>");
			sb.append(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
		sb.append("</date>");
		
		//invoiceType
		sb.append("<invoiceType>");
			sb.append(invoiceType.name());
		sb.append("</invoiceType>");
		
		//num
		sb.append("<num>");
			sb.append( Optional.ofNullable(num).orElse("") );
		sb.append("</num>");

		//operatorFullName
		sb.append("<operatorFullname>");
			sb.append( Optional.ofNullable(operatorFullName).orElse("") );
		sb.append("</operatorFullname>");
		
		//signature
		sb.append("<signature>");
		sb.append("</signature>");
		
		//signatureType
		sb.append("<signatureType>");
		sb.append("</signatureType>");
		
		//addInf
		sb.append("<addInf>");
		sb.append("</addInf>");
		
		//consignee
		sb.append("<consignee>");
			sb.append("<address>");
				sb.append( Optional.ofNullable(consignee.getAddress()).orElse("") );
			sb.append("</address>");		
			
			sb.append("<name>");
				sb.append( Optional.ofNullable(consignee.getName()).orElse("") );
			sb.append("</name>");		
	
			sb.append("<tin>");
				sb.append( Optional.ofNullable(consignee.getTin()).orElse("") );
			sb.append("</tin>");		
		sb.append("</consignee>");


		//consignor
		sb.append("<consignor>");
			sb.append("<address>");
				sb.append( Optional.ofNullable(consignor.getAddress()).orElse("") );
			sb.append("</address>");		
			
			sb.append("<name>");
				sb.append( Optional.ofNullable(consignor.getName()).orElse("") );
			sb.append("</name>");		
	
			sb.append("<tin>");
				sb.append( Optional.ofNullable(consignor.getTin()).orElse("") );
			sb.append("</tin>");		
		sb.append("</consignor>");

		
		//customers
		sb.append("<customers>");
			sb.append("<customer>");
				sb.append("<address>");
					sb.append( Optional.ofNullable(customer.getAddress()).orElse("") );
				sb.append("</address>");		
				
				sb.append("<name>");
					sb.append( Optional.ofNullable(customer.getName()).orElse("") );
				sb.append("</name>");		
		
				sb.append("<tin>");
					sb.append( Optional.ofNullable(customer.getTin()).orElse("") );
				sb.append("</tin>");					
			
				sb.append("<trailer>");
					sb.append( Optional.ofNullable(customer.getTrailer()).orElse("") );
				sb.append("</trailer>");		
			
			sb.append("</customer>");
		sb.append("</customers>");

		
		//deliveryTerm
		sb.append("<deliveryTerm>");
			sb.append("<contractDate>");
				sb.append(new SimpleDateFormat("dd.MM.yyyy").format(deliveryItem.getContractDate()));
			sb.append("</contractDate>");	

			sb.append("<contractNum>");
				sb.append( Optional.ofNullable(deliveryItem.getContractNum()).orElse("") );
			sb.append("</contractNum>");	
			
			sb.append("<destination>");
				sb.append( Optional.ofNullable(deliveryItem.getDestination()).orElse("") );
			sb.append("</destination>");	
			
			sb.append("<exerciseWay>");
				sb.append( Optional.ofNullable(deliveryItem.getExerciseWay()).orElse("") );
			sb.append("</exerciseWay>");	
			
			sb.append("<term>");
				sb.append( Optional.ofNullable(deliveryItem.getTerm()).orElse("") );
			sb.append("</term>");			
		sb.append("</deliveryTerm>");

		
		//productSet
		sb.append("<productSet>");
			sb.append("<currencyCode>");
				sb.append(currencyCode);
			sb.append("</currencyCode>");		

			
			getProducts().forEach(p -> {
				sb.append("<product>");
					sb.append("<description>");
						sb.append( Optional.ofNullable(p.getDescription()).orElse("") );
					sb.append("</description>");	
				
					sb.append("<ndsAmount>");
						sb.append( Optional.ofNullable(p.getNdsAmount()).orElse(0d) );
					sb.append("</ndsAmount>");	

					sb.append("<ndsRate>");
						sb.append( Optional.ofNullable(p.getNdsRate()).orElse(0d) );
					sb.append("</ndsRate>");
					
					sb.append("<priceWithTax>");
						sb.append( Optional.ofNullable(p.getPriceWithTax()).orElse(0d) );
					sb.append("</priceWithTax>");
					
					sb.append("<priceWithoutTax>");
						sb.append( Optional.ofNullable(p.getPriceWithoutTax()).orElse(0d) );
					sb.append("</priceWithoutTax>");

					sb.append("<quantity>");
						sb.append( Optional.ofNullable(p.getQuantity()).orElse(0d) );
					sb.append("</quantity>");

					sb.append("<turnoverSize>");
						sb.append( Optional.ofNullable(p.getTurnoverSize()).orElse(0d) );
					sb.append("</turnoverSize>");

					sb.append("<unitNomenclature>");
						sb.append( Optional.ofNullable(p.getUnitNomenclature()).orElse("") );
					sb.append("</unitNomenclature>");
					
					sb.append("<unitPrice>");
						sb.append( Optional.ofNullable(p.getUnitPrice()).orElse(0d) );
					sb.append("</unitPrice>");

				sb.append("</product>");					
			});
		
			sb.append("<totalNdsAmount>");
				sb.append(getProducts().stream().mapToDouble(d -> Optional.ofNullable(d.getNdsAmount()).orElse(0d) ).sum());
			sb.append("</totalNdsAmount>");

			sb.append("<totalPriceWithTax>");
				sb.append(getProducts().stream().mapToDouble(d -> Optional.ofNullable(d.getPriceWithoutTax()).orElse(0d) ).sum());
			sb.append("</totalPriceWithTax>");
	
			sb.append("<totalPriceWithoutTax>");
				sb.append(getProducts().stream().mapToDouble(d -> Optional.ofNullable(d.getPriceWithoutTax()).orElse(0d) ).sum());
			sb.append("</totalPriceWithoutTax>");
		
			sb.append("<totalTurnoverSize>");
				sb.append(getProducts().stream().mapToDouble(d -> Optional.ofNullable(d.getTurnoverSize()).orElse(0d) ).sum());
			sb.append("</totalTurnoverSize>");
		
		sb.append("</productSet>");

		
		//sellers
		sb.append("<sellers>");
			sb.append("<seller>");
				sb.append("<address>");
					sb.append(Optional.ofNullable(seller.getAddress()).orElse(""));
				sb.append("</address>");
				
				sb.append("<bank>");
					sb.append(Optional.ofNullable(seller.getBank()).orElse(""));
				sb.append("</bank>");		
	
				sb.append("<bik>");
					sb.append(Optional.ofNullable(seller.getBik()).orElse(""));
				sb.append("</bik>");		
			
				sb.append("<certificateNum>");
					sb.append(Optional.ofNullable(seller.getCertificateNum()).orElse(""));
				sb.append("</certificateNum>");		
	
				sb.append("<certificateSeries>");
					sb.append(Optional.ofNullable(seller.getCertificateSeries()).orElse(""));
				sb.append("</certificateSeries>");		
				
				sb.append("<deliveryDocDate>");
					sb.append(new SimpleDateFormat("dd.MM.yyyy").format(seller.getDeliveryDocDate()));
				sb.append("</deliveryDocDate>");
			
				sb.append("<deliveryDocNum>");
					sb.append(Optional.ofNullable(seller.getDeliveryDocNum()).orElse(""));
				sb.append("</deliveryDocNum>");	
			
				sb.append("<iik>");
					sb.append(Optional.ofNullable(seller.getIik()).orElse(""));
				sb.append("</iik>");	
	
				sb.append("<kbe>");
					sb.append(Optional.ofNullable(seller.getKbe()).orElse(""));
				sb.append("</kbe>");	
			
				sb.append("<name>");
					sb.append(Optional.ofNullable(seller.getName()).orElse(""));
				sb.append("</name>");	
	
				sb.append("<tin>");
					sb.append(Optional.ofNullable(seller.getTin()).orElse(""));
				sb.append("</tin>");	
			
				sb.append("<trailer>");
					sb.append(Optional.ofNullable(seller.getTrailer()).orElse(""));
				sb.append("</trailer>");
				
			sb.append("</seller>");
		sb.append("</sellers>");

		
		//date
		sb.append("<turnoverDate>");
			sb.append(new SimpleDateFormat("dd.MM.yyyy").format(turnoverDate));
		sb.append("</turnoverDate>");
		
		sb.append("</v1:invoice>");
		return sb.toString(); 
	}
	*/
}
