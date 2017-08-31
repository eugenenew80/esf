package esf.common.webapi.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class WebApplicationExceptionMapperImpl implements ExceptionMapper<WebApplicationException> { 
	
    @Override
    public Response toResponse(WebApplicationException exc) {
    	
    	String message = exc.getMessage();
    	if (message==null || message.equals(""))
    		message = exc.getClass().getName();
    	
    	return Response.status(exc.getResponse().getStatus())
	        .entity(new ErrorMessage(message))
	        .build();
    }
}
