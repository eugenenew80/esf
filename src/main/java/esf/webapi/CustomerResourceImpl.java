package esf.webapi;

import java.util.*;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import esf.common.repository.query.*;
import esf.entity.Customer;
import esf.entity.dto.CustomerDto;
import esf.service.CustomerService;


@RequestScoped
@Path("/customer")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class CustomerResourceImpl {
	
	
	public CustomerResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/CustomerDtoMapping.xml"));		
	}


	@GET
	@Path("/") 
	public Response getAll(@QueryParam("name") @DefaultValue("") String name) {
		
		Query query = QueryImpl.builder()
			.setParameter("name", new MyQueryParam("name", name + "%", ConditionType.LIKE))
			.build();		
		
		List<CustomerDto> list = customerService.find(query)
			.stream()
			.map( it-> mapper.map(it, CustomerDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<CustomerDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		Customer customer = customerService.findById(id);
		return Response.ok()
			.entity(mapper.map(customer, CustomerDto.class))
			.build();		
	}
	

	@GET
	@Path("/{name}")
	public Response getByName(@PathParam("name") String name) {		
		Customer customer = customerService.findByName(name);
		return Response.ok()
			.entity(mapper.map(customer, CustomerDto.class))
			.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin : \\d+}") 
	public Response getByTin(@PathParam("tin") String tin) {
		Customer customer = customerService.findByTin(tin);
		return Response.ok()
			.entity(mapper.map(customer, CustomerDto.class))
			.build();		
	}

	
	@Inject private CustomerService customerService;
	private DozerBeanMapper mapper;
}
