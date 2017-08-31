package esf.webapi.helper;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import esf.common.webapi.exception.EJBTransactionRolledbackExceptionMapperImpl;
import esf.common.webapi.exception.EntityNotFoundExceptionMapperImpl;
import esf.common.webapi.exception.ExceptionMapperImpl;
import esf.common.webapi.exception.InvalidArgumentExceptionMapperImpl;
import esf.common.webapi.exception.RepositryNotFoundExceptionMapperImpl;
import esf.common.webapi.exception.ValidationExceptionMapperImpl;
import esf.common.webapi.exception.WebApplicationExceptionMapperImpl;


public class AbstractResourceTest {
	private static Server server = null;
	private static AbstractBinder binder;
	private static Object resource;
	
	protected static void setBinder(AbstractBinder binder) {
		AbstractResourceTest.binder = binder;
	}
	
	protected static void setResource(Object resource) {
		AbstractResourceTest.resource = resource;
	}
	
	protected static void start(Binding binding) throws Exception {
		ResourceConfig config = new ResourceConfig();
		config.register(binder);
		config.register(resource);
		
		config.register(RepositryNotFoundExceptionMapperImpl.class);
		config.register(EntityNotFoundExceptionMapperImpl.class);
		config.register(ValidationExceptionMapperImpl.class);
		config.register(InvalidArgumentExceptionMapperImpl.class);
		
		config.register(WebApplicationExceptionMapperImpl.class);
		config.register(EJBTransactionRolledbackExceptionMapperImpl.class);
		config.register(ExceptionMapperImpl.class);
		
    	ServletHolder servlet = new ServletHolder(new ServletContainer(config));
    	server = new Server(2222);
    	ServletContextHandler context = new ServletContextHandler(server, "/*");
    	context.addServlet(servlet, "/esf/webapi/*");

    	server.start();		
	}
	
	
	protected static void stop() throws Exception {
		server.stop();
	}
}
