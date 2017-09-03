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
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		Customer customer = customerService.findByName(name);
		return Response.ok()
			.entity(mapper.map(customer, CustomerDto.class))
			.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin}") 
	public Response getByTin(@PathParam("tin") String tin) {
		Customer customer = customerService.findByTin(tin);
		return Response.ok()
			.entity(mapper.map(customer, CustomerDto.class))
			.build();		
	}

	
	@POST
	public Response create(CustomerDto customerDto) {
		Customer newCustomer = customerService.create(mapper.map(customerDto, Customer.class));	
		return Response.ok()
			.entity(mapper.map(newCustomer, CustomerDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, CustomerDto customerDto ) {
		Customer newCustomer = customerService.update(mapper.map(customerDto, Customer.class)); 
		return Response.ok()
			.entity(mapper.map(newCustomer, CustomerDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		customerService.delete(id);		
		return Response.noContent()
			.build();
	}	
	
	
	@Path("/{customerId : \\d+}/customerSite")
	public Class<CustomerSiteResourceImpl> getCustomerSite() {
		//return customerSiteResource;
		return CustomerSiteResourceImpl.class;
	}
	
	
	@Inject private CustomerService customerService;
	//@Inject private CustomerSiteResourceImpl customerSiteResource;
	private DozerBeanMapper mapper;
}
