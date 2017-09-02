package esf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import esf.repository.CompanyRepositoryTest;
import esf.repository.CustomerRepositoryTest;
import esf.service.*;
import esf.webapi.CompanyResourceTest;
import esf.webapi.CustomerResourceTest;
import esf.webapi.integration.CompanyResourceIntTest;
import esf.webapi.integration.CustomerResourceIntTest;

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
})
public class AllTests { }
