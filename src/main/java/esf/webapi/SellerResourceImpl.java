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
import esf.entity.Company;
import esf.entity.dto.SellerDto;
import esf.service.CompanyService;


@RequestScoped
@Path("/seller")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class SellerResourceImpl {
	
	
	public SellerResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/SellerDtoMapping.xml"));		
	}


	@GET
	@Path("/") 
	public Response getAll(@QueryParam("name") @DefaultValue("") String name) {
		
		Query query = QueryImpl.builder()
				.setParameter("name", new MyQueryParam("name", name + "%", ConditionType.LIKE))
			.build();		
		
		List<SellerDto> list = companyService.find(query)
			.stream()
			.map( it-> mapper.map(it, SellerDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<SellerDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{sellerId : \\d+}") 
	public Response getById(@PathParam("sellerId") Long sellerId) {
		
		if (sellerId==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		Company seller = companyService.findById(sellerId);
		if (seller==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(seller, SellerDto.class))
				.build();		
	}
	

	@GET
	@Path("/{sellerName}")
	public Response getByName(@PathParam("sellerName") String sellerName) {		
		if (sellerName==null)
			throw new WebApplicationException(SC_BAD_REQUEST);		
		
		Company seller = companyService.findByName(sellerName);
		if (seller==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(seller, SellerDto.class))
				.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin : \\d+}") 
	public Response getByTin(@PathParam("tin") String tin) {
		
		if (tin==null)
			throw new WebApplicationException(SC_BAD_REQUEST);
		
		Company seller = companyService.findByTin(tin);
		if (seller==null)
			throw new WebApplicationException(SC_NOT_FOUND);
		
		return Response.ok()
				.entity(mapper.map(seller, SellerDto.class))
				.build();		
	}

	
	@Inject private CompanyService companyService;
	private DozerBeanMapper mapper;
}
