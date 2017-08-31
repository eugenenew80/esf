package esf.webapi;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import esf.entity.Company;
import esf.entity.dto.CompanyDto;
import esf.service.CompanyService;

@RequestScoped
@Path("/company")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class CompanyResourceImpl {
	
	public CompanyResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/CompanyDtoMapping.xml"));
	} 


	@GET 
	public Response getAll(@QueryParam("name") @DefaultValue("") String name) {		
		Query query = QueryImpl.builder()
			.setParameter("name", new MyQueryParam("name", name + "%", ConditionType.LIKE))
			.build();		
		
		List<CompanyDto> list = companyService.find(query)
			.stream()
			.map( it-> mapper.map(it, CompanyDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<CompanyDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		Company company = companyService.findById(id);
		return Response.ok()
			.entity(mapper.map(company, CompanyDto.class))
			.build();		
	}
	

	@GET
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		Company company = companyService.findByName(name);
		return Response.ok()
			.entity(mapper.map(company, CompanyDto.class))
			.build();
	}
	
	
	@GET 
	@Path("/byTin/{tin}") 
	public Response getByTin(@PathParam("tin") String tin) {
		Company company = companyService.findByTin(tin);
		return Response.ok()
			.entity(mapper.map(company, CompanyDto.class))
			.build();		
	}

	
	@POST
	public Response create(CompanyDto companyDto) {
		Company newCompany = companyService.create(mapper.map(companyDto, Company.class));	
		return Response.ok()
			.entity(mapper.map(newCompany, CompanyDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{companyId : \\d+}") 
	public Response update(@PathParam("companyId") Long companyId, CompanyDto companyDto ) {
		Company updatedCompany = companyService.update(mapper.map(companyDto, Company.class)); 
		return Response.ok()
			.entity(mapper.map(updatedCompany, CompanyDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{companyId : \\d+}") 
	public Response delete(@PathParam("companyId") Long companyId) {
		companyService.delete(companyId);		
		return Response.noContent()
			.build();
	}	
	
	@Inject private CompanyService companyService;
	private DozerBeanMapper mapper;
}
