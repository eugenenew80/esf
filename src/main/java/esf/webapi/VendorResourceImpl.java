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
import esf.entity.Vendor;
import esf.entity.dto.VendorDto;
import esf.service.VendorService;


@RequestScoped
@Path("/vendor")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class VendorResourceImpl {
	
	
	public VendorResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/VendorDtoMapping.xml"));		
	}


	@GET
	@Path("/") 
	public Response getAll(@QueryParam("name") @DefaultValue("") String name) {
		
		Query query = QueryImpl.builder()
			.setParameter("name", new MyQueryParam("name", name + "%", ConditionType.LIKE))
			.build();		
		
		List<VendorDto> list = vendorService.find(query)
			.stream()
			.map( it-> mapper.map(it, VendorDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<VendorDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{customerId : \\d+}") 
	public Response getById(@PathParam("customerId") Long customerId) {
		
		if (customerId==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		Vendor vendor = vendorService.findById(customerId);
		if (vendor==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(vendor, VendorDto.class))
				.build();		
	}
	

	@GET
	@Path("/{customerName}")
	public Response getByName(@PathParam("customerName") String customerName) {		
		if (customerName==null)
			throw new WebApplicationException(SC_BAD_REQUEST);		
		
		Vendor vendor = vendorService.findByName(customerName);
		if (vendor==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(vendor, VendorDto.class))
				.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin : \\d+}") 
	public Response getByTin(@PathParam("tin") String tin) {
		
		if (tin==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		Vendor customer = vendorService.findByTin(tin);
		if (customer==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(customer, VendorDto.class))
				.build();		
	}

	 
	@Inject private VendorService vendorService;
	private DozerBeanMapper mapper;
}
