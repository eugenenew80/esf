package esf.common.webapi.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import esf.common.exception.EntityNotFoundException;

@Provider
public class EntityNotFoundExceptionMapperImpl implements ExceptionMapper<EntityNotFoundException> { 
	
    @Override
    public Response toResponse(EntityNotFoundException exc) {
    	return Response.status(exc.getStatusCode())
	        .entity(new ErrorMessage(exc.getCode(), exc.getMessage()))
	        .build();
    }
}
