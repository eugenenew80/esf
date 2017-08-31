package esf.common.webapi.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import esf.common.exception.InvalidArgumentException;

@Provider
public class InvalidArgumentExceptionMapperImpl implements ExceptionMapper<InvalidArgumentException> { 
	
    @Override
    public Response toResponse(InvalidArgumentException exc) {
    	return Response.status(exc.getStatusCode())
	        .entity(new ErrorMessage(exc.getCode(), exc.getMessage()))
	        .build();
    }
}
