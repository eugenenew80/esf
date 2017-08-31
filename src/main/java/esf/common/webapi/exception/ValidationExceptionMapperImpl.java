package esf.common.webapi.exception;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapperImpl implements ExceptionMapper<ValidationException> { 
	
    @Override
    public Response toResponse(ValidationException exc) {
    	return Response.status(500)
	        .entity(new ErrorMessage("validation-exception", exc.getMessage()))
	        .build();
    }
}
