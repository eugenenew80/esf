package esf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import esf.repository.*;
import esf.service.*;
import esf.webapi.*;
import esf.webapi.integration.*;

@RunWith(Suite.class)
@SuiteClasses({	
	CompanyRepositoryTest.class,
	CompanyServiceTest.class,	
	CompanyResourceTest.class,
	CompanyResourceIntTest.class,
	
	CustomerRepositoryTest.class,
	CustomerServiceTest.class,
	CustomerResourceTest.class,
	CustomerResourceIntTest.class,	
	
	VendorRepositoryTest.class,
	VendorServiceTest.class,
	VendorResourceTest.class,
	VendorResourceIntTest.class,
	
	VendorSiteRepositoryTest.class,
	VendorSiteServiceTest.class,
	
	CustomerSiteRepositoryTest.class,
	CustomerSiteServiceTest.class,
})
public class AllTests { }
