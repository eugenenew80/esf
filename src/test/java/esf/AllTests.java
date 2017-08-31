package esf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import esf.repository.CompanyRepositoryTest;
import esf.service.*;

@RunWith(Suite.class)
@SuiteClasses({
	CompanyServiceTest.class,
	CompanyRepositoryTest.class,
	esf.webapi.unit.CompanyResourceTest.class,
	esf.webapi.integration.CompanyResourceTest.class
})
public class AllTests { }
