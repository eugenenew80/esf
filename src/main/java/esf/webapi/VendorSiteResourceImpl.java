package esf.webapi;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import esf.entity.VendorSite;
import esf.entity.dto.VendorSiteDto;
import esf.service.VendorSiteService;

@RequestScoped
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class VendorSiteResourceImpl {
		
	public VendorSiteResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/VendorSiteDtoMapping.xml"));		
	}


	@GET
	public Response getAll(@PathParam("vendorId") Long vendorId) {
		List<VendorSiteDto> list = vendorSiteService.findByVendorId(vendorId)
			.stream()
			.map( it-> mapper.map(it, VendorSiteDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
			.entity(new GenericEntity<Collection<VendorSiteDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		VendorSite vendorSite = vendorSiteService.findById(id);
		return Response.ok()
			.entity(mapper.map(vendorSite, VendorSiteDto.class))
			.build();		
	}
	

	@POST
	public Response create(VendorSiteDto vendorSiteDto) {
		VendorSite newVendorSite = vendorSiteService.create(mapper.map(vendorSiteDto, VendorSite.class));	
		return Response.ok()
			.entity(mapper.map(newVendorSite, VendorSiteDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, VendorSiteDto vendorSiteDto ) {
		System.out.println(vendorSiteDto);
		
		VendorSite newVendorSite = vendorSiteService.update(mapper.map(vendorSiteDto, VendorSite.class)); 
		return Response.ok()
			.entity(mapper.map(newVendorSite, VendorSiteDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		vendorSiteService.delete(id);		
		return Response.noContent()
			.build();
	}	
	
	
	@Inject private VendorSiteService vendorSiteService;
	private DozerBeanMapper mapper;
}
