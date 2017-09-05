package esf.webapi;

import java.util.*;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import esf.entity.CustomerSite;
import esf.entity.dto.CustomerSiteDto;
import esf.service.CustomerSiteService;

@RequestScoped
public class CustomerSiteResourceImpl {
		
	public CustomerSiteResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/CustomerSiteDtoMapping.xml"));		
	}


	@GET
	public Response getAll(@PathParam("customerId") Long customerId) {
		List<CustomerSiteDto> list = customerSiteService.findByCustomerId(customerId)
			.stream()
			.map( it-> mapper.map(it, CustomerSiteDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
			.entity(new GenericEntity<Collection<CustomerSiteDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		CustomerSite customerSite = customerSiteService.findById(id);
		return Response.ok()
			.entity(mapper.map(customerSite, CustomerSiteDto.class))
			.build();		
	}
	

	@POST
	public Response create(CustomerSiteDto customerSiteDto) {
		CustomerSite newCustomerSite = customerSiteService.create(mapper.map(customerSiteDto, CustomerSite.class));	
		return Response.ok()
			.entity(mapper.map(newCustomerSite, CustomerSiteDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, CustomerSiteDto customerSiteDto ) {
		CustomerSite newCustomerSite = customerSiteService.update(mapper.map(customerSiteDto, CustomerSite.class)); 
		return Response.ok()
			.entity(mapper.map(newCustomerSite, CustomerSiteDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		customerSiteService.delete(id);		
		return Response.noContent()
			.build();
	}	
	
	
	@Inject private CustomerSiteService customerSiteService;
	private DozerBeanMapper mapper;
}
