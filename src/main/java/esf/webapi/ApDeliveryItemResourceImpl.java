package esf.webapi;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import esf.common.repository.query.*;
import esf.entity.ApDeliveryItem;
import esf.entity.dto.ApDeliveryItemDto;
import esf.service.ApDeliveryItemService;

@RequestScoped
public class ApDeliveryItemResourceImpl {
		
	public ApDeliveryItemResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/ApDeliveryItemDtoMapping.xml"));		
	}


	@GET
	public Response getAll(@PathParam("vendorId") Long vendorId) {
		Query query = QueryImpl.builder()
			.setParameter("vendor.id", new MyQueryParam("vendorId", vendorId, ConditionType.EQUALS))
			.build();		
		
		List<ApDeliveryItemDto> list = deliveryItemService.find(query)
			.stream()
			.map( it-> mapper.map(it, ApDeliveryItemDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
			.entity(new GenericEntity<Collection<ApDeliveryItemDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		ApDeliveryItem deliveryItem = deliveryItemService.findById(id);
		return Response.ok()
			.entity(mapper.map(deliveryItem, ApDeliveryItemDto.class))
			.build();		
	}
	

	@POST
	public Response create(ApDeliveryItemDto deliveryItemDto) {
		ApDeliveryItem newApDeliveryItem = deliveryItemService.create(mapper.map(deliveryItemDto, ApDeliveryItem.class));	
		return Response.ok()
			.entity(mapper.map(newApDeliveryItem, ApDeliveryItemDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, ApDeliveryItemDto deliveryItemDto ) {
		ApDeliveryItem newApDeliveryItem = deliveryItemService.update(mapper.map(deliveryItemDto, ApDeliveryItem.class)); 
		return Response.ok()
			.entity(mapper.map(newApDeliveryItem, ApDeliveryItemDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		deliveryItemService.delete(id);		
		return Response.noContent()
			.build();
	}	
	
	
	@Inject private ApDeliveryItemService deliveryItemService;
	private DozerBeanMapper mapper;
}
