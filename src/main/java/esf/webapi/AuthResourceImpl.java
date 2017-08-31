package esf.webapi;

import static javax.servlet.http.HttpServletResponse.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.dozer.DozerBeanMapper;

import esf.entity.dto.ResponsibilityDto;
import esf.service.AuthService;



@RequestScoped
@Path("/auth")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class AuthResourceImpl {

	
	public AuthResourceImpl() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/ResponsibilityDtoMapping.xml"));		
	}
	
	
	@GET
	@Path("/") 
	public Response auth(@HeaderParam("userName") String userName, @HeaderParam("password") String password) {

		try {
			userName = new String(Base64.decodeBase64(userName), "UTF-8");
			password = new String(Base64.decodeBase64(password), "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			userName=null;
			password=null;
		}
		
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) 
			throw new WebApplicationException(SC_UNAUTHORIZED);
		

		Long userId = authService.auth(userName, password);
		if (userId==-1L)
			throw new WebApplicationException(SC_UNAUTHORIZED);
		
		List<ResponsibilityDto> list = authService.getResponsibility(userId)
			.stream()
			.map( it-> mapper.map(it, ResponsibilityDto.class) )
			.collect(Collectors.toList());
		
		if (list.size()==0) 
			throw new WebApplicationException(SC_UNAUTHORIZED);
		
		return Response.ok()
			.entity(new GenericEntity<Collection<ResponsibilityDto>>(list){})
			.build();		
	}

	
	@Inject private AuthService authService;
	private DozerBeanMapper mapper;
}
