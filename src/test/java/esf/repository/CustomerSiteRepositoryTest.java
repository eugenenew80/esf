package esf.repository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
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

import esf.repository.CustomerSiteRepository;
import esf.repository.impl.CustomerSiteRepositoryImpl;
import esf.common.repository.query.ConditionType;
import esf.common.repository.query.MyQueryParam;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.CustomerSite;
import esf.entity.CustomerSite;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import static esf.helper.EntitiesHelper.*;


public class CustomerSiteRepositoryTest {
	private static CustomerSiteRepository repository;
	private static DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_customer_site.xml"));
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		dbUnitHelper = new DBUnitHelper(); 
		repository = new CustomerSiteRepositoryImpl(dbUnitHelper.getEntityManager());
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
	public void theListCustomerSitesMayBeSelected() {	
		List<CustomerSite> customerSites = repository.selectAll();
		
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(3));
		
		assertThat(customerSites.get(0), is(not(nullValue())));
		assertThat(customerSites.get(1), is(not(nullValue())));
		assertThat(customerSites.get(2), is(not(nullValue())));
		
		assertThat(customerSites.get(0).getId(), is(equalTo(1L)));
		assertThat(customerSites.get(1).getId(), is(equalTo(2L)));
		assertThat(customerSites.get(2).getId(), is(equalTo(3L)));
		assertCustomerSite(customerSites.get(0));
	}
	
	
	@Test
	public void theListCustomerSitesMayBeSelectedByQuery() {
		Query query = QueryImpl.builder()
			.setParameter("customer.id", new MyQueryParam("customerId", 1L, ConditionType.EQUALS))
			.build();		
		
		List<CustomerSite> customerSites = repository.select(query);			
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(1));
		assertThat(customerSites.get(0), is(not(nullValue())));
		assertThat(customerSites.get(0).getId(), is(equalTo(1L)));
		assertCustomerSite(customerSites.get(0));		
		
		
		query = QueryImpl.builder().build();					
		customerSites = repository.select(query);			
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(3));
	}	
	
	
	@Test
	public void existingCustomerSiteMayBeSelectedById() {
		CustomerSite customerSite = repository.selectById(1L);
		assertThat(customerSite, is(not(nullValue())));
		assertCustomerSite(customerSite);
		
		customerSite = repository.selectById(4L);
		assertThat(customerSite, is(nullValue()));		
	}
	
			
	@Test
	public void existingCustomerSiteMayBeSelectedByContractNum() {
		CustomerSite customerSite = repository.selectByContractNum(1L, "001");
		assertThat(customerSite, is(not(nullValue())));
		assertCustomerSite(customerSite);
	}
	

	@Test
	public void theListCustomerSitesMayBeCustomerId() {	
		List<CustomerSite> customerSites = repository.selectByCustomerId(1L);
		
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(1));
		
		assertThat(customerSites.get(0), is(not(nullValue())));
		assertThat(customerSites.get(0).getCustomer(), is(not(nullValue())));
		assertThat(customerSites.get(0).getId(), is(equalTo(1L)));
		assertThat(customerSites.get(0).getCustomer().getId(), is(equalTo(1L)));
		assertCustomerSite(customerSites.get(0));
	}
	

	@Test
	public void newCustomerSiteMayBeInserted() {
		CustomerSite origCustomerSite = newCustomerSite(null);
		CustomerSite customerSite = repository.insert(origCustomerSite);				
		assertThat(customerSite.getId(), is(not(nullValue())));
		
		customerSite = repository.selectById(customerSite.getId());
		assertThat(customerSite, is(not(nullValue())));
		assertCustomerSite(customerSite);		
	}	
	
	
	@Test
	public void existingCustomerSiteMayBeUpdated() {
		CustomerSite origCustomerSite = repository.selectById(1L);;	
		origCustomerSite.setContractNum(origCustomerSite.getContractNum() + " (нов)");
		
		CustomerSite customerSite = repository.update(origCustomerSite);				
		assertThat(customerSite, is(not(nullValue())));
		
		customerSite = repository.selectById(customerSite.getId());
		assertCustomerSite(origCustomerSite, customerSite);
	}	
	
	
	@Test
	public void existingCustomerSiteMayBeDeleted() {
		boolean result = repository.delete(4L);				
		assertThat(result, is(equalTo(false)));
		 
		result = repository.delete(1L);				
		assertThat(result, is(equalTo(true)));
		
		CustomerSite customerSite = repository.selectById(1L);
		assertThat(customerSite, is(nullValue()));
	}	
	
	
	//Fail cases
	
	@Test(expected = UnsupportedOperationException.class )
	public void failMethodSelectByName() {
		repository.selectByName("Бла бла бла");
	}
} 
