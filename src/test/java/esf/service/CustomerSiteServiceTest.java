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
import esf.service.CustomerSiteService;
import esf.service.impl.CustomerSiteServiceImpl;
import esf.repository.CustomerSiteRepository;
import esf.entity.CustomerSite;
import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;


public class CustomerSiteServiceTest {
	CustomerSiteRepository mockRepository;
	CustomerSiteService service;
	ValidatorFactory factory;
	Validator validator;
	
	@Before
	public void setUp() throws Exception {
		factory = Validation.buildDefaultValidatorFactory(); 
		validator = factory.getValidator(); 

		mockRepository = mock(CustomerSiteRepository.class);
		service = new CustomerSiteServiceImpl(mockRepository, validator);	
	}

	
	@After
	public void tearDown() throws Exception {
		factory.close();
	}

	
	//Success cases
	
	@Test
	public void theListCustomerSitesMayBeFound() {
		when(mockRepository.selectAll())
			.thenReturn(Arrays.asList(newCustomerSite(1L), newCustomerSite(2L), newCustomerSite(3L) ));
		
		List<CustomerSite> customerSites = service.findAll();	
		
		verify(mockRepository, times(1)).selectAll();		
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(3));
		assertThat(customerSites.get(0).getId(), is(equalTo(1L)));
		assertThat(customerSites.get(1).getId(), is(equalTo(2L)));
		assertThat(customerSites.get(2).getId(), is(equalTo(3L)));
		assertCustomerSite(customerSites.get(0));		
	}
	

	@Test
	public void theListCustomerSitesMayBeFoundByQuery() {
		Query query = QueryImpl.builder().build();		
		when(mockRepository.select(query))
			.thenReturn(Arrays.asList(newCustomerSite(1L), newCustomerSite(2L), newCustomerSite(3L) ));
		
		List<CustomerSite> customerSites = service.find(query);	
		
		verify(mockRepository, times(1)).select(query);		
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(3));
		assertThat(customerSites.get(0).getId(), is(equalTo(1L)));
		assertThat(customerSites.get(1).getId(), is(equalTo(2L)));
		assertThat(customerSites.get(2).getId(), is(equalTo(3L)));
		assertCustomerSite(customerSites.get(0));		
	}	
	

	@Test
	public void theListCustomerSitesMayBeFoundCustomerId() {
		when(mockRepository.selectByCustomerId(1L))
			.thenReturn(Arrays.asList(newCustomerSite(1L)));
		
		List<CustomerSite> customerSites = service.findByCustomerId(1L);	
		
		verify(mockRepository, times(1)).selectByCustomerId(1L);		
		assertThat(customerSites, is(not(nullValue())));
		assertThat(customerSites, is(not(empty())));
		assertThat(customerSites, hasSize(1));
		assertThat(customerSites.get(0).getId(), is(equalTo(1L)));
		assertCustomerSite(customerSites.get(0));		
	}
	
	
	@Test
	public void existingCustomerSiteMayBeFoundById() {
		when(mockRepository.selectById(1L))
			.thenReturn(newCustomerSite(1L));		
		
		CustomerSite customerSites = service.findById(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		assertThat(customerSites, is(not(nullValue())));
		assertCustomerSite(customerSites);
	}
	

	@Test
	public void existingCustomerSiteMayBeFoundByContractNum() {
		when(mockRepository.selectByContractNum(1L, CUSTOMER_SITE_NUM))
			.thenReturn(newCustomerSite(1L));		
		
		CustomerSite customerSites = service.findByContractNum(1L, CUSTOMER_SITE_NUM);		
		
		verify(mockRepository, times(1)).selectByContractNum(1L, CUSTOMER_SITE_NUM);
		assertThat(customerSites, is(not(nullValue())));
		assertCustomerSite(customerSites);
	}
	
	
	@Test
	public void newCustomerSiteMayBeCreated() {
		CustomerSite origCustomerSite = newCustomerSite(null);
		when(mockRepository.insert(origCustomerSite))
			.thenReturn(newCustomerSite(1L));
		
		CustomerSite customerSites = service.create(origCustomerSite);		
		
		verify(mockRepository, times(1)).insert(origCustomerSite);		
		assertThat(customerSites.getId(), is(not(nullValue())));
		assertCustomerSite(customerSites);
	}

	
	@Test
	public void existingCustomerSiteMayBeUpdated() {
		CustomerSite origCustomerSite = newCustomerSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomerSite);
		when(mockRepository.update(anyObject()))
			.then(returnsFirstArg());
		
		CustomerSite customerSites = service.update(origCustomerSite);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).update(origCustomerSite);
		assertThat(customerSites, is(not(nullValue())));
		assertCustomerSite(customerSites);
	}

	
	@Test
	public void existingCustomerSiteMayBeDeleted() {
		when(mockRepository.selectById(1L))
			.thenReturn(newCustomerSite(1L));
		when(mockRepository.delete(anyLong()))
			.thenReturn(true);		
				
		boolean result = service.delete(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).delete(1L);
		assertThat(result, is(equalTo(true)));
	}

	
	//Fail cases
	
	 
	//Unsupported Operation 
	
	@Test(expected=UnsupportedOperationException.class)
	public void existingCustomerSiteMayBeFoundByName() {
		when(mockRepository.selectByName(anyString()))
			.thenThrow(new UnsupportedOperationException());		
		
		service.findByName("Бла бла бла");		
	}	
	
	
	//Bean Validation
	
	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfContractNumIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomerSite);
		
		CustomerSite newCustomerSite = newCustomerSite(1L);
		newCustomerSite.setContractNum(null);		
		
		service.update(newCustomerSite);
	}


	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfContractDateIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomerSite);
		
		CustomerSite newCustomerSite = newCustomerSite(1L);
		newCustomerSite.setContractDate(null);	
		
		service.update(newCustomerSite);
	}
	

	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfCustomerIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCustomerSite);
		
		CustomerSite newCustomerSite = newCustomerSite(1L);
		newCustomerSite.setCustomer(null);		
		
		service.update(newCustomerSite);
	}

	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfContractNumIsNull() {		
		CustomerSite newCustomerSite = newCustomerSite(null);
		newCustomerSite.setContractNum(null);		
		
		service.create(newCustomerSite);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfContractDateIsNull() {		
		CustomerSite newCustomerSite = newCustomerSite(null);
		newCustomerSite.setContractDate(null);		
		
		service.create(newCustomerSite);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfCustomerIsNull() {		
		CustomerSite newCustomerSite = newCustomerSite(null);
		newCustomerSite.setCustomer(null);		
		
		service.create(newCustomerSite);
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
	public void failMethodFindByCustomerIdIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByCustomerId(1L);	
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByIdIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findById(1L);		
	}


	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByContractNumIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByContractNum(1L, CUSTOMER_SITE_NUM);		
	}

	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByNameIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByName(CUSTOMER_NAME);		
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodCreateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.create(newCustomerSite(null));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodUpdateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.update(newCustomerSite(1L));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodDeleteIfRepositoryIsNull() {		
		service.setRepository(null);
		service.delete(1L);		
	}

	
	//Call with invalid arguments

	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByCustomerIdIfCustomerIdIsNull() {		
		service.findByCustomerId(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByContractNumIfCustomerIdIsNull() {		
		service.findByContractNum(null, CUSTOMER_SITE_NUM);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByContractNumIfContractNumIsNull() {		
		service.findByContractNum(1L, null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByIdIfCustomerSiteIdIsNull() {		
		service.findById(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByNameIfCustomerSiteNameIsNull() {
		service.findByName(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfCustomerSiteIsNull() {		
		service.create(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfCustomerSiteIsNull() {		
		service.update(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodDeleteIfCustomerSiteIdIsNull() {
		service.delete(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfCustomerSiteHasNotNullId() {		
		service.create(newCustomerSite(1L));		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfCustomerSiteHasNullId() {		
		service.update(newCustomerSite(null));		
	}

	
		
	//Entity not found
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByIdIfCustomerSiteIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.findById(1L);
	}


	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByContractNumIfCustomerSiteIsNotExist() {		
		when(mockRepository.selectByContractNum(anyLong(), anyString()))
			.thenReturn(null);		
		
		service.findByContractNum(1L, CUSTOMER_SITE_NUM);
	}

	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodUpdateIfCustomerSitedIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.update(newCustomerSite(1L));
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodDeleteIfCustomerSitedIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.delete(1L);		
	}
}
