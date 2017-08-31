package esf.webapi;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;

import esf.common.repository.query.ConditionType;
import esf.common.repository.query.MyQueryParam;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.InvoiceStatus;
import esf.entity.dto.ArInvoiceDto;
import esf.entity.dto.ListInvoicesDto;
import esf.entity.invoice.ar.ArInvoice;
import esf.service.ArInvoiceService;


@RequestScoped
@Path("/ar_invoice")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class ArInvoiceResourceImpl {
	
	public ArInvoiceResourceImpl() { 
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/ArInvoiceDtoMapping.xml"));
	}
	
	
	@GET
	@Path("/") 
	public Response getAll(
							@QueryParam("status") @DefaultValue("all") String status, 
							@QueryParam("contractNum") String contractNum, 
							@QueryParam("contractDate") String contractDateStr,
							@QueryParam("startDate") String startDateStr,
							@QueryParam("endDate") String endDateStr,
							@QueryParam("consigneeId") Long consigneeId,
							@QueryParam("orgId") @DefaultValue("-1") Long sellerId,
							@QueryParam("userId") @DefaultValue("-1") Long userId
	) {
		
		
		Date startDate = null;
		if (StringUtils.isEmpty(startDateStr)) {
			Calendar cal = Calendar.getInstance(); 
			cal.add(Calendar.MONTH, -1);			
			startDate = cal.getTime(); 
		}
		else
			startDate  = toDateFromJson(startDateStr);
		
		
		Query query = QueryImpl.builder()		
			.setParameter("status",  					!status.equals("all") ? InvoiceStatus.valueOf(status.toUpperCase()) : null)
			.setParameter("deliveryItem.contractNum",  	StringUtils.isNotEmpty(contractNum) ? new MyQueryParam("contractNum", contractNum + "%", ConditionType.LIKE) : null)
			.setParameter("deliveryItem.contractDate", 	StringUtils.isNotEmpty(contractDateStr) ? new MyQueryParam("contractDate", toDateFromJson(contractDateStr), ConditionType.EQUALS) : null)
			.setParameter("invoiceDate",				startDate!=null ? new MyQueryParam("startInvoiceDate", startDate, ConditionType.GTE) : null)
			.setParameter("invoiceDate", 				StringUtils.isNotEmpty(endDateStr) ? new MyQueryParam("endInvoiceDate", toDateFromJson(endDateStr), ConditionType.LTE) : null)
			.setParameter("seller.id", 					new MyQueryParam("sellerId", sellerId, ConditionType.EQUALS))
			.setParameter("userId", 					userId!=-1L ? userId : null)
			.setParameter("consignee.id", 				new MyQueryParam("consigneeId", consigneeId, ConditionType.EQUALS))
			.setOrderBy("t.id desc")
			.build();
		
		List<ArInvoiceDto> list = invoiceService.find(query)
				.stream()
				.map( it-> mapper.map(it, ArInvoiceDto.class) )
				.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<ArInvoiceDto>>(list){})
				.build();
	}
	
	
	
	@GET 
	@Path("/{invoiceId : \\d+}") 
	public Response getById(@PathParam("invoiceId") Long invoiceId) {
		
		if (invoiceId==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		ArInvoice invoice = invoiceService.findById(invoiceId);
		if (invoice==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(invoice, ArInvoiceDto.class))
				.build();		
	}
	
	
	
	@PUT
	@Path("/export/xml")
	public Response exportToXML(ListInvoicesDto list) {
		if (list==null || list.invoices==null || list.invoices.isEmpty())
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<ns2:invoiceContainer xmlns:ns2= \"namespace.esf\" xmlns:v1=\"namespace.v1\">");
		sb.append("<invoiceSet>");
		
		for (Long invoiceId : list.invoices) 
			sb.append(invoiceService.toXML(invoiceId));

		sb.append("</invoiceSet>");
		sb.append("</ns2:invoiceContainer>");
		
		for (Long invoiceId : list.invoices) {
			ArInvoice invoice = invoiceService.findById(invoiceId);			
			invoice.setStatus(InvoiceStatus.EXPORTED);
			invoiceService.update(invoice);
		}
		
		return Response.ok()
				.entity( "{ \"data\": \"" + Base64.encodeBase64String(sb.toString().getBytes(Charset.forName("UTF-8"))) + "\" }" )
				.build();
	}
	
	
	@PUT
	@Path("/import/external")
	public Response importFromExternalSystem(@QueryParam("sellerId") @DefaultValue("102") Long sellerId) {
		invoiceService.fromExternalSystem(sellerId);
		
		return Response.ok()
				.entity("")
				.build();
	}
	
	
	private Date toDateFromJson(String dateStr) {
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr.substring(0, dateStr.indexOf("T")));
		} 
		catch (ParseException e) {
			date=null;
		}
		
		return date;
	}
	
	
	@Inject private ArInvoiceService invoiceService;
	private DozerBeanMapper mapper;
}
