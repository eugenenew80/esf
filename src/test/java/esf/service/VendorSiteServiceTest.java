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
import esf.service.VendorSiteService;
import esf.service.impl.VendorSiteServiceImpl;
import esf.repository.VendorSiteRepository;
import esf.entity.VendorSite;
import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;


public class VendorSiteServiceTest {
	VendorSiteRepository mockRepository;
	VendorSiteService service;
	ValidatorFactory factory;
	Validator validator;
	
	@Before
	public void setUp() throws Exception {
		factory = Validation.buildDefaultValidatorFactory(); 
		validator = factory.getValidator(); 

		mockRepository = mock(VendorSiteRepository.class);
		service = new VendorSiteServiceImpl(mockRepository, validator);	
	}

	
	@After
	public void tearDown() throws Exception {
		factory.close();
	}

	
	//Success cases
	
	@Test
	public void theListVendorSitesMayBeFound() {
		when(mockRepository.selectAll())
			.thenReturn(Arrays.asList(newVendorSite(1L), newVendorSite(2L), newVendorSite(3L) ));
		
		List<VendorSite> vendorSites = service.findAll();	
		
		verify(mockRepository, times(1)).selectAll();		
		assertThat(vendorSites, is(not(nullValue())));
		assertThat(vendorSites, is(not(empty())));
		assertThat(vendorSites, hasSize(3));
		assertThat(vendorSites.get(0).getId(), is(equalTo(1L)));
		assertThat(vendorSites.get(1).getId(), is(equalTo(2L)));
		assertThat(vendorSites.get(2).getId(), is(equalTo(3L)));
		assertVendorSite(vendorSites.get(0));		
	}
	

	@Test
	public void theListVendorSitesMayBeFoundByQuery() {
		Query query = QueryImpl.builder().build();		
		when(mockRepository.select(query))
			.thenReturn(Arrays.asList(newVendorSite(1L), newVendorSite(2L), newVendorSite(3L) ));
		
		List<VendorSite> vendorSites = service.find(query);	
		
		verify(mockRepository, times(1)).select(query);		
		assertThat(vendorSites, is(not(nullValue())));
		assertThat(vendorSites, is(not(empty())));
		assertThat(vendorSites, hasSize(3));
		assertThat(vendorSites.get(0).getId(), is(equalTo(1L)));
		assertThat(vendorSites.get(1).getId(), is(equalTo(2L)));
		assertThat(vendorSites.get(2).getId(), is(equalTo(3L)));
		assertVendorSite(vendorSites.get(0));		
	}	
	

	@Test
	public void theListVendorSitesMayBeFoundVendorId() {
		when(mockRepository.selectByVendorId(1L))
			.thenReturn(Arrays.asList(newVendorSite(1L)));
		
		List<VendorSite> vendorSites = service.findByVendorId(1L);	
		
		verify(mockRepository, times(1)).selectByVendorId(1L);		
		assertThat(vendorSites, is(not(nullValue())));
		assertThat(vendorSites, is(not(empty())));
		assertThat(vendorSites, hasSize(1));
		assertThat(vendorSites.get(0).getId(), is(equalTo(1L)));
		assertVendorSite(vendorSites.get(0));		
	}
	
	
	@Test
	public void existingVendorSiteMayBeFoundById() {
		when(mockRepository.selectById(1L))
			.thenReturn(newVendorSite(1L));		
		
		VendorSite vendorSites = service.findById(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		assertThat(vendorSites, is(not(nullValue())));
		assertVendorSite(vendorSites);
	}
	

	@Test
	public void existingVendorSiteMayBeFoundByContractNum() {
		when(mockRepository.selectByContractNum(1L, VENDOR_SITE_NUM))
			.thenReturn(newVendorSite(1L));		
		
		VendorSite vendorSites = service.findByContractNum(1L, VENDOR_SITE_NUM);		
		
		verify(mockRepository, times(1)).selectByContractNum(1L, VENDOR_SITE_NUM);
		assertThat(vendorSites, is(not(nullValue())));
		assertVendorSite(vendorSites);
	}
	
	
	@Test
	public void newVendorSiteMayBeCreated() {
		VendorSite origVendorSite = newVendorSite(null);
		when(mockRepository.insert(origVendorSite))
			.thenReturn(newVendorSite(1L));
		
		VendorSite vendorSites = service.create(origVendorSite);		
		
		verify(mockRepository, times(1)).insert(origVendorSite);		
		assertThat(vendorSites.getId(), is(not(nullValue())));
		assertVendorSite(vendorSites);
	}

	
	@Test
	public void existingVendorSiteMayBeUpdated() {
		VendorSite origVendorSite = newVendorSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendorSite);
		when(mockRepository.update(anyObject()))
			.then(returnsFirstArg());
		
		VendorSite vendorSites = service.update(origVendorSite);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).update(origVendorSite);
		assertThat(vendorSites, is(not(nullValue())));
		assertVendorSite(vendorSites);
	}

	
	@Test
	public void existingVendorSiteMayBeDeleted() {
		when(mockRepository.selectById(1L))
			.thenReturn(newVendorSite(1L));
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
	public void existingVendorSiteMayBeFoundByName() {
		when(mockRepository.selectByName(anyString()))
			.thenThrow(new UnsupportedOperationException());		
		
		service.findByName("Бла бла бла");		
	}	
	
	
	//Bean Validation
	
	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfContractNumIsNull() {		
		VendorSite origVendorSite = newVendorSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendorSite);
		
		VendorSite newVendorSite = newVendorSite(1L);
		newVendorSite.setContractNum(null);		
		
		service.update(newVendorSite);
	}


	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfContractDateIsNull() {		
		VendorSite origVendorSite = newVendorSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendorSite);
		
		VendorSite newVendorSite = newVendorSite(1L);
		newVendorSite.setContractDate(null);	
		
		service.update(newVendorSite);
	}
	

	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfVendorIsNull() {		
		VendorSite origVendorSite = newVendorSite(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendorSite);
		
		VendorSite newVendorSite = newVendorSite(1L);
		newVendorSite.setVendor(null);		
		
		service.update(newVendorSite);
	}

	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfContractNumIsNull() {		
		VendorSite newVendorSite = newVendorSite(null);
		newVendorSite.setContractNum(null);		
		
		service.create(newVendorSite);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfContractDateIsNull() {		
		VendorSite newVendorSite = newVendorSite(null);
		newVendorSite.setContractDate(null);		
		
		service.create(newVendorSite);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfVendorIsNull() {		
		VendorSite newVendorSite = newVendorSite(null);
		newVendorSite.setVendor(null);		
		
		service.create(newVendorSite);
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
	public void failMethodFindByVendorIdIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByVendorId(1L);	
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByIdIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findById(1L);		
	}


	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByContractNumIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByContractNum(1L, VENDOR_SITE_NUM);		
	}

	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByNameIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByName(VENDOR_NAME);		
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodCreateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.create(newVendorSite(null));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodUpdateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.update(newVendorSite(1L));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodDeleteIfRepositoryIsNull() {		
		service.setRepository(null);
		service.delete(1L);		
	}

	
	//Call with invalid arguments

	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByVendorIdIfVendorIdIsNull() {		
		service.findByVendorId(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByContractNumIfVendorIdIsNull() {		
		service.findByContractNum(null, VENDOR_SITE_NUM);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByContractNumIfContractNumIsNull() {		
		service.findByContractNum(1L, null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByIdIfVendorSiteIdIsNull() {		
		service.findById(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByNameIfVendorSiteNameIsNull() {
		service.findByName(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfVendorSiteIsNull() {		
		service.create(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfVendorSiteIsNull() {		
		service.update(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodDeleteIfVendorSiteIdIsNull() {
		service.delete(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfVendorSiteHasNotNullId() {		
		service.create(newVendorSite(1L));		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfVendorSiteHasNullId() {		
		service.update(newVendorSite(null));		
	}

	
		
	//Entity not found
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByIdIfVendorSiteIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.findById(1L);
	}


	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByContractNumIfVendorSiteIsNotExist() {		
		when(mockRepository.selectByContractNum(anyLong(), anyString()))
			.thenReturn(null);		
		
		service.findByContractNum(1L, VENDOR_SITE_NUM);
	}

	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodUpdateIfVendorSitedIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.update(newVendorSite(1L));
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodDeleteIfVendorSitedIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.delete(1L);		
	}
}
