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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import esf.repository.CustomerRepository;
import esf.repository.impl.CustomerRepositoryImpl;
import esf.common.repository.query.ConditionType;
import esf.common.repository.query.MyQueryParam;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.Customer;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import static esf.helper.EntitiesHelper.*;


public class CustomerRepositoryTest {
	private static CustomerRepository repository;
	private static DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_customer.xml"));
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		dbUnitHelper = new DBUnitHelper(); 
		repository = new CustomerRepositoryImpl(dbUnitHelper.getEntityManager());;
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
	public void theListCustomersMayBeSelected() throws Exception {	
		List<Customer> customers = repository.selectAll();
		
		assertThat(customers, is(not(nullValue())));
		assertThat(customers, is(not(empty())));
		assertThat(customers, hasSize(3));
		
		assertThat(customers.get(0), is(not(nullValue())));
		assertThat(customers.get(1), is(not(nullValue())));
		assertThat(customers.get(2), is(not(nullValue())));
		
		assertThat(customers.get(0).getId(), is(equalTo(1L)));
		assertThat(customers.get(1).getId(), is(equalTo(2L)));
		assertThat(customers.get(2).getId(), is(equalTo(3L)));
		assertCustomer(customers.get(0));
	}
	
	
	@Test
	public void theListCustomersMayBeSelectedByQuery() {
		Query query = QueryImpl.builder()
			.setParameter("name", new MyQueryParam("name", "Заказчик 1%", ConditionType.LIKE))
			.build();		
		
		List<Customer> customers = repository.select(query);			
		assertThat(customers, is(not(nullValue())));
		assertThat(customers, is(not(empty())));
		assertThat(customers, hasSize(1));
		assertThat(customers.get(0), is(not(nullValue())));
		assertThat(customers.get(0).getId(), is(equalTo(1L)));
		assertCustomer(customers.get(0));		
		
		
		query = QueryImpl.builder().build();					
		customers = repository.select(query);			
		assertThat(customers, is(not(nullValue())));
		assertThat(customers, is(not(empty())));
		assertThat(customers, hasSize(3));
	}	
	
	
	@Test
	public void existingCustomerMayBeSelectedById() throws Exception {
		Customer customer = repository.selectById(1L);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);
		
		customer = repository.selectById(4L);
		assertThat(customer, is(nullValue()));		
	}
	
	
	@Test
	public void existingCustomerMayBeSelectedByName() throws Exception {
		Customer customer = repository.selectByName(CUSTOMER_NAME);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);

		customer = repository.selectByName("бла бла бла");
		assertThat(customer, is(nullValue()));	
	}

	
	@Test
	public void existingCustomerMayBeSelectedByTin() throws Exception {
		Customer customer = repository.selectByTin(CUSTOMER_TIN);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);

		customer = repository.selectByTin("999999999999");
		assertThat(customer, is(nullValue()));		
	}
	
	
	@Test
	public void newCustomerMayBeInserted() {
		Customer origCustomer = newCustomer(null);
		
		Customer customer = repository.insert(origCustomer);				
		assertThat(customer.getId(), is(not(nullValue())));
		
		customer = repository.selectById(customer.getId());
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);		
	}	
	
	
	@Test
	public void existingCustomerMayBeUpdated() {
		Customer origCustomer = repository.selectById(1L);;	
		origCustomer.setName(origCustomer.getName() + " (нов)");
		
		Customer customer = repository.update(origCustomer);				
		assertThat(customer, is(not(nullValue())));
		
		customer = repository.selectById(customer.getId());
		assertCustomer(origCustomer, customer);
	}	
	
	
	@Test
	public void existingCustomerMayBeDeleted() {
		boolean result = repository.delete(4L);				
		assertThat(result, is(equalTo(false)));
		 
		result = repository.delete(1L);				
		assertThat(result, is(equalTo(true)));
		
		Customer customer = repository.selectById(1L);
		assertThat(customer, is(nullValue()));
	}	
} 
