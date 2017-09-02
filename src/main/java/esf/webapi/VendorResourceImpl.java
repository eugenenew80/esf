package esf.webapi;

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
		Vendor customer = vendorService.findByTin(tin);
		return Response.ok()
			.entity(mapper.map(customer, VendorDto.class))
			.build();		
	}

	 
	@Inject private VendorService vendorService;
	private DozerBeanMapper mapper;
}
