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
import esf.service.CompanyService;
import esf.service.impl.CompanyServiceImpl;
import esf.repository.CompanyRepository;
import esf.entity.Company;
import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;


public class CompanyServiceTest {
	CompanyRepository mockRepository;
	CompanyService service;
	ValidatorFactory factory;
	Validator validator;
	
	@Before
	public void setUp() throws Exception {
		factory = Validation.buildDefaultValidatorFactory(); 
		validator = factory.getValidator(); 

		mockRepository = mock(CompanyRepository.class);
		service = new CompanyServiceImpl(mockRepository, validator);	
	}

	
	@After
	public void tearDown() throws Exception {
		factory.close();
	}

	
	//Success cases
	
	@Test
	public void theListCompaniesMayBeFound() {
		when(mockRepository.selectAll())
			.thenReturn(Arrays.asList(newCompany(1L), newCompany(2L), newCompany(3L) ));
		
		List<Company> companies = service.findAll();	
		
		verify(mockRepository, times(1)).selectAll();		
		assertThat(companies, is(not(nullValue())));
		assertThat(companies, is(not(empty())));
		assertThat(companies, hasSize(3));
		assertThat(companies.get(0).getId(), is(equalTo(1L)));
		assertThat(companies.get(1).getId(), is(equalTo(2L)));
		assertThat(companies.get(2).getId(), is(equalTo(3L)));
		assertCompany(companies.get(0));		
	}
	

	@Test
	public void theListCompaniesMayBeFoundByQuery() {
		Query query = QueryImpl.builder().build();		
		when(mockRepository.select(query))
			.thenReturn(Arrays.asList(newCompany(1L), newCompany(2L), newCompany(3L) ));
		
		List<Company> companies = service.find(query);	
		
		verify(mockRepository, times(1)).select(query);		
		assertThat(companies, is(not(nullValue())));
		assertThat(companies, is(not(empty())));
		assertThat(companies, hasSize(3));
		assertThat(companies.get(0).getId(), is(equalTo(1L)));
		assertThat(companies.get(1).getId(), is(equalTo(2L)));
		assertThat(companies.get(2).getId(), is(equalTo(3L)));
		assertCompany(companies.get(0));		
	}	
	
	
	@Test
	public void existingCompanyMayBeFoundById() {
		when(mockRepository.selectById(1L))
			.thenReturn(newCompany(1L));		
		
		Company company = service.findById(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);
	}
	 

	@Test
	public void existingCompanyMayBeFoundByName() {
		when(mockRepository.selectByName(COMPANY_NAME))
			.thenReturn(newCompany(1L));		
		
		Company company = service.findByName(COMPANY_NAME);		
		
		verify(mockRepository, times(1)).selectByName(COMPANY_NAME);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);
	}
	

	@Test
	public void existingCompanyMayBeFoundByTin() {
		when(mockRepository.selectByTin(COMPANY_TIN))
			.thenReturn(newCompany(1L));		
		
		Company company = service.findByTin(COMPANY_TIN);		
		
		verify(mockRepository, times(1)).selectByTin(COMPANY_TIN);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);
	}	
	
	
	@Test
	public void newCompanyMayBeCreated() {
		Company origCompany = newCompany(null);
		when(mockRepository.insert(origCompany))
			.thenReturn(newCompany(1L));
		
		Company company = service.create(origCompany);		
		
		verify(mockRepository, times(1)).insert(origCompany);		
		assertThat(company.getId(), is(not(nullValue())));
		assertCompany(company);
	}

	
	@Test
	public void existingCompanyMayBeUpdated() {
		Company origCompany = newCompany(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCompany);
		when(mockRepository.update(anyObject()))
			.then(returnsFirstArg());
		
		Company company = service.update(origCompany);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).update(origCompany);
		assertThat(company, is(not(nullValue())));
		assertCompany(company);
	}

	
	@Test
	public void existingCompanyMayBeDeleted() {
		when(mockRepository.selectById(1L))
			.thenReturn(newCompany(1L));
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
		Company origCompany = newCompany(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCompany);
		
		Company newCompany = newCompany(1L);
		newCompany.setName(null);		
		
		service.update(newCompany);
	}


	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfTinIsNull() {		
		Company origCompany = newCompany(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origCompany);
		
		Company newCompany = newCompany(1L);
		newCompany.setTin(null);		
		
		service.update(newCompany);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfNameIsNull() {		
		Company newCompany = newCompany(null);
		newCompany.setName(null);		
		
		service.create(newCompany);
	}


	@Test(expected=ValidationException.class)
	public void failMethodCreateIfTinIsNull() {		
		Company newCompany = newCompany(null);
		newCompany.setTin(null);		
		
		service.create(newCompany);
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
		service.create(newCompany(null));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodUpdateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.update(newCompany(1L));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodDeleteIfRepositoryIsNull() {		
		service.setRepository(null);
		service.delete(1L);		
	}

	
	//Call with invalid arguments
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByIdIfCompanyIdIsNull() {		
		service.findById(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByNameIfCompanyNameIsNull() {
		service.findByName(null);		
	}
	

	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByTinIfCompanyNameIsNull() {
		service.findByTin(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfCompanyIsNull() {		
		service.create(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfCompanyIsNull() {		
		service.update(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodDeleteIfCompanyIdIsNull() {
		service.delete(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfCompanyHasNotNullId() {		
		service.create(newCompany(1L));		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfCompanyHasNullId() {		
		service.update(newCompany(null));		
	}

	
		
	//Entity not found
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByIdIfCompanyIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.findById(1L);
	}

	

	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByNameIfCompanyNameIsNotExist() {		
		when(mockRepository.selectByName(anyString()))
			.thenReturn(null);		
		
		service.findByName(COMPANY_NAME);
	}


	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByNameIfCompanyTinIsNotExist() {		
		when(mockRepository.selectByTin(anyString()))
			.thenReturn(null);		
		
		service.findByTin(COMPANY_TIN);
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodUpdateIfCompanydIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.update(newCompany(1L));
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodDeleteIfCompanydIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.delete(1L);		
	}
}
