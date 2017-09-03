package esf.common.webapi.config;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import esf.common.webapi.exception.*;
import esf.service.impl.AuthServiceImpl;
import esf.webapi.*;

@ApplicationPath("/webapi")
public class JaxRsConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> resources = new HashSet<Class<?>>();
		
		resources.add(ArInvoiceResourceImpl.class);
		resources.add(ApInvoiceResourceImpl.class);
		resources.add(CompanyResourceImpl.class);
		resources.add(CustomerResourceImpl.class);
		resources.add(VendorResourceImpl.class);
		resources.add(AuthServiceImpl.class);
		resources.add(VendorSiteResourceImpl.class);
		resources.add(CustomerSiteResourceImpl.class);
		
		resources.add(RepositryNotFoundExceptionMapperImpl.class);
		resources.add(EntityNotFoundExceptionMapperImpl.class);
		resources.add(ValidationExceptionMapperImpl.class);
		resources.add(InvalidArgumentExceptionMapperImpl.class);
		
        resources.add(WebApplicationExceptionMapperImpl.class);
        resources.add(EJBTransactionRolledbackExceptionMapperImpl.class);
        resources.add(ExceptionMapperImpl.class);
        
		return resources;
	}	
}
