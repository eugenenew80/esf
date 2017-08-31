package esf.common.webapi.exception;

import javax.ejb.EJBTransactionRolledbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class EJBTransactionRolledbackExceptionMapperImpl implements ExceptionMapper<EJBTransactionRolledbackException> { 
	
    @Override
    public Response toResponse(EJBTransactionRolledbackException exc) {
    	    	
    	Throwable cause = exc;
    	if (exc.getCause()!=null) {
    		cause = exc.getCause();
    		if (exc.getCause().getCause()!=null)
    			cause =exc.getCause().getCause();
    	} 
    			
    	String message = cause.getMessage();

    	if (cause instanceof ConstraintViolationException) 
    		message = "Bean validation exception: ";
    		for (ConstraintViolation<?> v: ((ConstraintViolationException) cause).getConstraintViolations()) { 
	    		message += v.getPropertyPath() + ": " +v.getMessage() + "; ";    	
    		}
    	
    	if (message==null || message.equals(""))
    		message = exc.getClass().getName();
    	
    	return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(message))
                .build();
    }
}
