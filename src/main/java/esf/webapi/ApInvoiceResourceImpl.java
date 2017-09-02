package esf.webapi;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import esf.entity.ApInvoice;
import esf.entity.InvoiceStatus;
import esf.entity.InvoiceType;
import esf.entity.dto.ApInvoiceDto;
import esf.entity.dto.RawDataDto;
import esf.service.ApInvoiceService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;




@RequestScoped
@Path("/ap_invoice")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class ApInvoiceResourceImpl {
	
	public ApInvoiceResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/ApInvoiceDtoMapping.xml"));
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
							@QueryParam("customerId") @DefaultValue("102") Long customerId
	) {
		
		Query query = QueryImpl.builder()		
			.setParameter("status",  					!status.equals("all") ? InvoiceStatus.valueOf(status.toUpperCase()) : null)
			.setParameter("deliveryItem.contractNum",  	StringUtils.isNotEmpty(contractNum) ? new MyQueryParam("contractNum", contractNum + "%", ConditionType.LIKE) : null)
			.setParameter("deliveryItem.contractDate", 	StringUtils.isNotEmpty(contractDateStr) ? new MyQueryParam("contractDate", toDateFromJson(contractDateStr), ConditionType.EQUALS) : null)
			.setParameter("invoiceDate", 				StringUtils.isNotEmpty(startDateStr) ? new MyQueryParam("startInvoiceDate", toDateFromJson(startDateStr), ConditionType.GTE) : null)
			.setParameter("invoiceDate", 				StringUtils.isNotEmpty(endDateStr) ? new MyQueryParam("endInvoiceDate", toDateFromJson(endDateStr), ConditionType.LTE) : null)
			.setParameter("customer.id", 				new MyQueryParam("customerId", customerId, ConditionType.EQUALS))
			.setParameter("consignee.id", 				new MyQueryParam("consigneeId", consigneeId, ConditionType.EQUALS))
			.setOrderBy("t.id desc")
			.build();
		
		List<ApInvoiceDto> list = invoiceService.find(query)
				.stream()
				.map( it-> mapper.map(it, ApInvoiceDto.class) )
				.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<ApInvoiceDto>>(list){})
				.build();
	}
	
		
	@GET 
	@Path("/{invoiceId : \\d+}") 
	public Response getById(@PathParam("invoiceId") Long invoiceId) {
		
		if (invoiceId==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		ApInvoice invoice = invoiceService.findById(invoiceId);
		if (invoice==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(invoice, ApInvoiceDto.class))
				.build();		
	}
		
	
	@PUT
	@Path("/import")
	public Response importDocs(RawDataDto rawData) throws Exception {
		
		String data = new String(Base64.decodeBase64(rawData.getData()));	
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
				
		ByteArrayInputStream input =  new ByteArrayInputStream(data.getBytes("UTF-8"));
		Document doc = builder.parse(input);
		

		

		Node parentNode = doc.getDocumentElement().getParentNode().getFirstChild();
		
		if (parentNode.getNodeName().equals("esf:invoiceInfoContainer") ) {
					
			for(int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
				if (parentNode.getChildNodes().item(i).getNodeName().equals("invoiceSet")) {
					Node invoiceSetNode = parentNode.getChildNodes().item(i);
					for(int j = 0; j < invoiceSetNode.getChildNodes().getLength(); j++) {
						if (invoiceSetNode.getChildNodes().item(j).getNodeName().equals("invoiceInfo")) {
							ApInvoice inv = new ApInvoice();
							Node invoiceInfoNode = invoiceSetNode.getChildNodes().item(j);
							for(int k = 0; k < invoiceInfoNode.getChildNodes().getLength(); k++) {
								
								Node node = invoiceInfoNode.getChildNodes().item(k);
								//if (node.getNodeName().equals("deliveryDate")) 
								//	inv.setInvoiceDate( toDateFromJson(node.getFirstChild().getTextContent()) );
								
								if (node.getNodeName().equals("v1:invoice")) {
									for(int l = 0; l < node.getChildNodes().getLength(); l++) {
										
										if (node.getChildNodes().item(l).getNodeName().equals("date"))
											inv.setInvoiceDate( new SimpleDateFormat("dd.MM.yyyy").parse(node.getChildNodes().item(l).getFirstChild().getTextContent()));
										
										if (node.getChildNodes().item(l).getNodeName().equals("invoiceType"))
											inv.setInvoiceType(  InvoiceType.valueOf(node.getChildNodes().item(l).getFirstChild().getTextContent()) );
										
										if (node.getChildNodes().item(l).getNodeName().equals("num")) 
											inv.setNum( node.getChildNodes().item(l).getFirstChild().getTextContent() );
										
										if (node.getChildNodes().item(l).getNodeName().equals("operatorFullname")) 
											inv.setOperatorFullName( node.getChildNodes().item(l).getFirstChild().getTextContent() );	
										
										
										if (node.getChildNodes().item(l).getNodeName().equals("consignee")) {
											
										}
									}
								}
							}
							
							System.out.println(inv.getInvoiceDate());	
						}
					}
					
					

				}
			}
		}

		
		/*
		//Parsing XML string
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new java.io.StringReader(new String(Base64.decodeBase64(rawData.getData()), "UTF-8"))));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		Document doc = parser.getDocument();
		
		NodeList nodes =  doc.getDocumentElement().getParentNode().getFirstChild().getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++) {
			//System.out.println(nodes.item(i).getNodeName());
		}
		*/
		
		return Response.ok()
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
	
	
	@Inject private ApInvoiceService invoiceService;
	private DozerBeanMapper mapper;
}
