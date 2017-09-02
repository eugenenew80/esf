package esf.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import org.junit.*;
import java.util.*;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static esf.helper.EntitiesHelper.*;
import esf.service.CustomerService;
import esf.service.impl.CustomerServiceImpl;
import esf.repository.CustomerRepository;
import esf.entity.Customer;
import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;


public class CustomerServiceTest {
	CustomerRepository mockRepository;
	CustomerService service;
	ValidatorFactory factory;
	Validator validator;
	
	@Before
	public void setUp() throws Exception {
		factory = Validation.buildDefaultValidatorFactory(); 
		validator = factory.getValidator(); 

		mockRepository = mock(CustomerRepository.class);
		service = new CustomerServiceImpl(mockRepository, validator);	
	}

	
	@After
	public void tearDown() throws Exception {
		factory.close();
	}

	
	//Success cases
	
	@Test
	public void theListCompaniesMayBeFound() {
		when(mockRepository.selectAll())
			.thenReturn(Arrays.asList(newCustomer(1L), newCustomer(2L), newCustomer(3L) ));
		
		List<Customer> customers = service.findAll();	
		
		verify(mockRepository, times(1)).selectAll();		
		assertThat(customers, is(not(nullValue())));
		assertThat(customers, is(not(empty())));
		assertThat(customers, hasSize(3));
		assertThat(customers.get(0).getId(), is(equalTo(1L)));
		assertThat(customers.get(1).getId(), is(equalTo(2L)));
		assertThat(customers.get(2).getId(), is(equalTo(3L)));
		assertCustomer(customers.get(0));		
	}
	

	@Test
	public void theListCompaniesMayBeFoundByQuery() {
		Query query = QueryImpl.builder().build();		
		when(mockRepository.select(query))
			.thenReturn(Arrays.asList(newCustomer(1L), newCustomer(2L), newCustomer(3L) ));
		
		List<Customer> customers = service.find(query);	
		
		verify(mockRepository, times(1)).select(query);		
		assertThat(customers, is(not(nullValue())));
		assertThat(customers, is(not(empty())));
		assertThat(customers, hasSize(3));
		assertThat(customers.get(0).getId(), is(equalTo(1L)));
		assertThat(customers.get(1).getId(), is(equalTo(2L)));
		assertThat(customers.get(2).getId(), is(equalTo(3L)));
		assertCustomer(customers.get(0));		
	}	
	
	
	@Test
	public void existingCustomerMayBeFoundById() {
		when(mockRepository.selectById(1L))
			.thenReturn(newCustomer(1L));		
		
		Customer customer = service.findById(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);
	}
	 

	@Test
	public void existingCustomerMayBeFoundByName() {
		when(mockRepository.selectByName(COMPANY_NAME))
			.thenReturn(newCustomer(1L));		
		
		Customer customer = service.findByName(COMPANY_NAME);		
		
		verify(mockRepository, times(1)).selectByName(COMPANY_NAME);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);
	}
	

	@Test
	public void existingCustomerMayBeFoundByTin() {
		when(mockRepository.selectByTin(COMPANY_TIN))
			.thenReturn(newCustomer(1L));		
		
		Customer customer = service.findByTin(COMPANY_TIN);		
		
		verify(mockRepository, times(1)).selectByTin(COMPANY_TIN);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);
	}	
	
	
	@Test
	public void newCustomerMayBeCreated() {
		Customer origCustomer = newCustomer(null);
		when(mockRepository.insert(origCustomer))
			.thenReturn(newCustomer(1L));
		
		Customer customer = service.create(origCustomer);		
		
		verify(mockRepository, times(1)).insert(origCustomer);		
		assertThat(customer.getId(), is(not(nullValue())));
		assertCustomer(customer);
	}

	
	@Test
	public void existingCustomerMayBeUpdated() {
		Customer origCustomer = newCustomer(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomer);
		when(mockRepository.update(anyObject()))
			.then(returnsFirstArg());
		
		Customer customer = service.update(origCustomer);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).update(origCustomer);
		assertThat(customer, is(not(nullValue())));
		assertCustomer(customer);
	}

	
	@Test
	public void existingCustomerMayBeDeleted() {
		when(mockRepository.selectById(1L))
			.thenReturn(newCustomer(1L));
		when(mockRepository.delete(anyLong()))
			.thenReturn(true);		
				
		boolean result = service.delete(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).delete(1L);
		assertThat(result, is(equalTo(true)));
	}

	
	
	//Bean Validation
	
	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfNameIsNull() {		
		Customer origCustomer = newCustomer(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomer);
		
		Customer newCustomer = newCustomer(1L);
		newCustomer.setName(null);		
		
		service.update(newCustomer);
	}


	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfTinIsNull() {		
		Customer origCustomer = newCustomer(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomer);
		
		Customer newCustomer = newCustomer(1L);
		newCustomer.setTin(null);		
		
		service.update(newCustomer);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfNameIsNull() {		
		Customer newCustomer = newCustomer(null);
		newCustomer.setName(null);		
		
		service.create(newCustomer);
	}


	@Test(expected=ValidationException.class)
	public void failMethodCreateIfTinIsNull() {		
		Customer newCustomer = newCustomer(null);
		newCustomer.setTin(null);		
		
		service.create(newCustomer);
	}
	
	
	//Repository not found
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindAllIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findAll();	
	}


	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindIfRepositoryIsNull() {		
		service.setRepository(null);
		service.find(QueryImpl.builder().build());	
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByIdIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findById(1L);		
	}


	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByNameIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByName(COMPANY_NAME);		
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByTinIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByTin(COMPANY_TIN);		
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodCreateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.create(newCustomer(null));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodUpdateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.update(newCustomer(1L));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodDeleteIfRepositoryIsNull() {		
		service.setRepository(null);
		service.delete(1L);		
	}

	
	//Call with invalid arguments
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByIdIfCustomerIdIsNull() {		
		service.findById(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByNameIfCustomerNameIsNull() {
		service.findByName(null);		
	}
	

	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByTinIfCustomerNameIsNull() {
		service.findByTin(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfCustomerIsNull() {		
		service.create(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfCustomerIsNull() {		
		service.update(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodDeleteIfCustomerIdIsNull() {
		service.delete(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfCustomerHasNotNullId() {		
		service.create(newCustomer(1L));		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfCustomerHasNullId() {		
		service.update(newCustomer(null));		
	}

	
		
	//Entity not found
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByIdIfCustomerIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.findById(1L);
	}

	

	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByNameIfCustomerNameIsNotExist() {		
		when(mockRepository.selectByName(anyString()))
			.thenReturn(null);		
		
		service.findByName(COMPANY_NAME);
	}


	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByNameIfCustomerTinIsNotExist() {		
		when(mockRepository.selectByTin(anyString()))
			.thenReturn(null);		
		
		service.findByTin(COMPANY_TIN);
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodUpdateIfCustomerdIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.update(newCustomer(1L));
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodDeleteIfCustomerdIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.delete(1L);		
	}
}
