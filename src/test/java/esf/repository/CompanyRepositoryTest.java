package esf.repository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import esf.repository.CompanyRepository;
import esf.repository.impl.CompanyRepositoryImpl;
import esf.common.repository.query.ConditionType;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.Company;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import static esf.helper.EntitiesHelper.*;


public class CompanyRepositoryTest {
	private static CompanyRepository repository;
	private static DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_company.xml"));
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		dbUnitHelper = new DBUnitHelper(); 
		repository = new CompanyRepositoryImpl(dbUnitHelper.getEntityManager());;
	}
		
	@AfterClass
	public static void tearDownClass() throws Exception {
		dbUnitHelper.closeDatabase();
	}
	
	
	@Before
	public void setUp() throws Exception {		
		dbUnitHelper.beginTransaction();
		dbUnitHelper.cleanAndInsert(dataSetList);
	}
	
	@After
	public void tearDown() {
		dbUnitHelper.commitTransaction(true);
	}

	
	
	//Success cases
	
	@Test
	public void theListCompanysMayBeSelected() throws Exception {	
		List<Company> companies = repository.selectAll();
		
		assertThat(companies, is(not(nullValue())));
		assertThat(companies, is(not(empty())));
		assertThat(companies, hasSize(3));
		
		assertThat(companies.get(0), is(not(nullValue())));
		assertThat(companies.get(1), is(not(nullValue())));
		assertThat(companies.get(2), is(not(nullValue())));
		
		assertThat(companies.get(0).getId(), is(equalTo(1L)));
		assertThat(companies.get(1).getId(), is(equalTo(2L)));
		assertThat(companies.get(2).getId(), is(equalTo(3L)));
		assertCompany(companies.get(0));
	}
	
	
	@Test
	public void theListCompaniesMayBeSelectedByQuery() {
		Query query = QueryImpl.builder()
			.setParameter("name", new esf.common.repository.query.MyQueryParam("name", "Компания 1%", ConditionType.LIKE))
			.build();		
		
		List<Company> companies = repository.select(query);			
		assertThat(companies, is(not(nullValue())));
		assertThat(companies, is(not(empty())));
		assertThat(companies, hasSize(1));
		assertThat(companies.get(0), is(not(nullValue())));
		assertThat(companies.get(0).getId(), is(equalTo(1L)));
		assertCompany(companies.get(0));		
		
		
		query = QueryImpl.builder().build();					
		companies = repository.select(query);			
		assertThat(companies, is(not(nullValue())));
		assertThat(companies, is(not(empty())));
		assertThat(companies, hasSize(3));
	}	
	
	
	@Test
	public void existingCompanyMayBeSelectedById() throws Exception {
		Company company = repository.selectById(1L);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);
		
		company = repository.selectById(4L);
		assertThat(company, is(nullValue()));		
	}
	
	
	@Test
	public void existingCompanyMayBeSelectedByName() throws Exception {
		Company company = repository.selectByName(COMPANY_NAME);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);

		company = repository.selectByName("бла бла бла");
		assertThat(company, is(nullValue()));	
	}

	
	@Test
	public void existingCompanyMayBeSelectedByTin() throws Exception {
		Company company = repository.selectByTin(COMPANY_TIN);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);

		company = repository.selectByTin("999999999999");
		assertThat(company, is(nullValue()));		
	}
	
	
	@Test
	public void newCompanyMayBeInserted() {
		Company origCompany = newCompany(null);
		
		Company company = repository.insert(origCompany);				
		assertThat(company.getId(), is(not(nullValue())));
		
		company = repository.selectById(company.getId());
		assertThat(company, is(not(nullValue())));
		assertCompany(company);		
	}	
	
	
	@Test
	public void existingCompanyMayBeUpdated() {
		Company origCompany = repository.selectById(1L);;	
		origCompany.setName(origCompany.getName() + " (нов)");
		
		Company company = repository.update(origCompany);				
		assertThat(company, is(not(nullValue())));
		
		company = repository.selectById(company.getId());
		assertCompany(origCompany, company);
	}	
	
	
	@Test
	public void existingCompanyMayBeDeleted() {
		boolean result = repository.delete(4L);				
		assertThat(result, is(equalTo(false)));
		 
		result = repository.delete(1L);				
		assertThat(result, is(equalTo(true)));
		
		Company company = repository.selectById(1L);
		assertThat(company, is(nullValue()));
	}	
} 
