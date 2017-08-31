package esf.webapi;

import static javax.servlet.http.HttpServletResponse.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.dozer.DozerBeanMapper;

import esf.common.repository.query.ConditionType;
import esf.common.repository.query.MyQueryParam;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.Customer;
import esf.entity.dto.ConsigneeDto;
import esf.service.CustomerService;


@RequestScoped
@Path("/consignee")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class ConsigneeResourceImpl {
	
	
	public ConsigneeResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/ConsigneeDtoMapping.xml"));		
	}


	@GET
	@Path("/") 
	public Response getAll(@QueryParam("name") @DefaultValue("") String name) {
		
		Query query = QueryImpl.builder()
			.setParameter("name", new MyQueryParam("name", name + "%", ConditionType.LIKE))
			.setOrderBy("t.name")
			.build();		
		
		List<ConsigneeDto> list = customerService.find(query)
			.stream()
			.map( it-> mapper.map(it, ConsigneeDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<ConsigneeDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{consigneeId : \\d+}") 
	public Response getById(@PathParam("consigneeId") Long consigneeId) {
		
		if (consigneeId==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		Customer consignee = customerService.findById(consigneeId);
		if (consignee==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(consignee, ConsigneeDto.class))
				.build();		
	}
	

	@GET
	@Path("/{consigneeName}")
	public Response getByName(@PathParam("consigneeName") String consigneeName) {		
		if (consigneeName==null)
			throw new WebApplicationException(SC_BAD_REQUEST);		
		
		Customer consignor = customerService.findByName(consigneeName);
		if (consignor==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(consignor, ConsigneeDto.class))
				.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin : \\d+}") 
	public Response getByTin(@PathParam("tin") String tin) {
		
		if (tin==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		Customer consignee = customerService.findByTin(tin);
		if (consignee==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(consignee, ConsigneeDto.class))
				.build();		
	}

	
	@Inject private CustomerService customerService;
	private DozerBeanMapper mapper;
}
