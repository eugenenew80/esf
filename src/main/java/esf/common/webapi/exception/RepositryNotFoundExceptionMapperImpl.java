package esf.common.webapi.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import esf.common.exception.RepositoryNotFoundException;

@Provider
public class RepositryNotFoundExceptionMapperImpl implements ExceptionMapper<RepositoryNotFoundException> { 
	
    @Override
    public Response toResponse(RepositoryNotFoundException exc) {
    	return Response.status(exc.getStatusCode())
	        .entity(new ErrorMessage(exc.getCode(), exc.getMessage()))
	        .build();
    }
}
