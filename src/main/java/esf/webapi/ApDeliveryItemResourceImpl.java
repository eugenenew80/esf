package esf.webapi;

import java.util.*;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import esf.common.repository.query.*;
import esf.entity.Vendor;
import esf.entity.dto.VendorDto;
import esf.service.VendorService;


@RequestScoped
@Path("/vendor")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class ApDeliveryItemResourceImpl {
	
	
	public ApDeliveryItemResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/VendorDtoMapping.xml"));		
	}


	@GET
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
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		Vendor vendor = vendorService.findById(id);
		return Response.ok()
			.entity(mapper.map(vendor, VendorDto.class))
			.build();		
	}
	

	@GET
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		Vendor vendor = vendorService.findByName(name);
		return Response.ok()
			.entity(mapper.map(vendor, VendorDto.class))
			.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin}") 
	public Response getByTin(@PathParam("tin") String tin) {
		Vendor vendor = vendorService.findByTin(tin);
		return Response.ok()
			.entity(mapper.map(vendor, VendorDto.class))
			.build();		
	}


	@POST
	public Response create(VendorDto vendorDto) {
		Vendor newVendor = vendorService.create(mapper.map(vendorDto, Vendor.class));	
		return Response.ok()
			.entity(mapper.map(newVendor, VendorDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, VendorDto vendorDto ) {
		Vendor newVendor = vendorService.update(mapper.map(vendorDto, Vendor.class)); 
		return Response.ok()
			.entity(mapper.map(newVendor, VendorDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		vendorService.delete(id);		
		return Response.noContent()
			.build();
	}	
	
	
	@Inject private VendorService vendorService;
	private DozerBeanMapper mapper;
}
