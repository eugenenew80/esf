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
import esf.service.VendorService;
import esf.service.impl.VendorServiceImpl;
import esf.repository.VendorRepository;
import esf.entity.Vendor;
import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;


public class VendorServiceTest {
	VendorRepository mockRepository;
	VendorService service;
	ValidatorFactory factory;
	Validator validator;
	
	@Before
	public void setUp() throws Exception {
		factory = Validation.buildDefaultValidatorFactory(); 
		validator = factory.getValidator(); 

		mockRepository = mock(VendorRepository.class);
		service = new VendorServiceImpl(mockRepository, validator);	
	}

	
	@After
	public void tearDown() throws Exception {
		factory.close();
	}

	
	//Success cases
	
	@Test
	public void theListVendorsMayBeFound() {
		when(mockRepository.selectAll())
			.thenReturn(Arrays.asList(newVendor(1L), newVendor(2L), newVendor(3L) ));
		
		List<Vendor> vendors = service.findAll();	
		
		verify(mockRepository, times(1)).selectAll();		
		assertThat(vendors, is(not(nullValue())));
		assertThat(vendors, is(not(empty())));
		assertThat(vendors, hasSize(3));
		assertThat(vendors.get(0).getId(), is(equalTo(1L)));
		assertThat(vendors.get(1).getId(), is(equalTo(2L)));
		assertThat(vendors.get(2).getId(), is(equalTo(3L)));
		assertVendor(vendors.get(0));		
	}
	

	@Test
	public void theListVendorsMayBeFoundByQuery() {
		Query query = QueryImpl.builder().build();		
		when(mockRepository.select(query))
			.thenReturn(Arrays.asList(newVendor(1L), newVendor(2L), newVendor(3L) ));
		
		List<Vendor> vendors = service.find(query);	
		
		verify(mockRepository, times(1)).select(query);		
		assertThat(vendors, is(not(nullValue())));
		assertThat(vendors, is(not(empty())));
		assertThat(vendors, hasSize(3));
		assertThat(vendors.get(0).getId(), is(equalTo(1L)));
		assertThat(vendors.get(1).getId(), is(equalTo(2L)));
		assertThat(vendors.get(2).getId(), is(equalTo(3L)));
		assertVendor(vendors.get(0));		
	}	
	
	
	@Test
	public void existingVendorMayBeFoundById() {
		when(mockRepository.selectById(1L))
			.thenReturn(newVendor(1L));		
		
		Vendor vendor = service.findById(1L);		
		
		verify(mockRepository, times(1)).selectById(1L);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);
	}
	 

	@Test
	public void existingVendorMayBeFoundByName() {
		when(mockRepository.selectByName(VENDOR_NAME))
			.thenReturn(newVendor(1L));		
		
		Vendor vendor = service.findByName(VENDOR_NAME);		
		
		verify(mockRepository, times(1)).selectByName(VENDOR_NAME);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);
	}
	

	@Test
	public void existingVendorMayBeFoundByTin() {
		when(mockRepository.selectByTin(VENDOR_TIN))
			.thenReturn(newVendor(1L));		
		
		Vendor vendor = service.findByTin(VENDOR_TIN);		
		
		verify(mockRepository, times(1)).selectByTin(VENDOR_TIN);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);
	}	
	
	
	@Test
	public void newVendorMayBeCreated() {
		Vendor origVendor = newVendor(null);
		when(mockRepository.insert(origVendor))
			.thenReturn(newVendor(1L));
		
		Vendor vendor = service.create(origVendor);		
		
		verify(mockRepository, times(1)).insert(origVendor);		
		assertThat(vendor.getId(), is(not(nullValue())));
		assertVendor(vendor);
	}

	
	@Test
	public void existingVendorMayBeUpdated() {
		Vendor origVendor = newVendor(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendor);
		when(mockRepository.update(anyObject()))
			.then(returnsFirstArg());
		
		Vendor vendor = service.update(origVendor);		
		
		verify(mockRepository, times(1)).selectById(1L);
		verify(mockRepository, times(1)).update(origVendor);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);
	}

	
	@Test
	public void existingVendorMayBeDeleted() {
		when(mockRepository.selectById(1L))
			.thenReturn(newVendor(1L));
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
		Vendor origVendor = newVendor(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendor);
		
		Vendor newVendor = newVendor(1L);
		newVendor.setName(null);		
		
		service.update(newVendor);
	}


	@Test(expected=ValidationException.class)
	public void failMethodUpdateIfTinIsNull() {		
		Vendor origVendor = newVendor(1L);		
		when(mockRepository.selectById(1L))
			.thenReturn(origVendor);
		
		Vendor newVendor = newVendor(1L);
		newVendor.setTin(null);		
		
		service.update(newVendor);
	}
	
	
	@Test(expected=ValidationException.class)
	public void failMethodCreateIfNameIsNull() {		
		Vendor newVendor = newVendor(null);
		newVendor.setName(null);		
		
		service.create(newVendor);
	}


	@Test(expected=ValidationException.class)
	public void failMethodCreateIfTinIsNull() {		
		Vendor newVendor = newVendor(null);
		newVendor.setTin(null);		
		
		service.create(newVendor);
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
		service.findByName(VENDOR_NAME);		
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodFindByTinIfRepositoryIsNull() {		
		service.setRepository(null);
		service.findByTin(VENDOR_TIN);		
	}
	
	
	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodCreateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.create(newVendor(null));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodUpdateIfRepositoryIsNull() {		
		service.setRepository(null);
		service.update(newVendor(1L));	
	}
	

	@Test(expected=RepositoryNotFoundException.class)
	public void failMethodDeleteIfRepositoryIsNull() {		
		service.setRepository(null);
		service.delete(1L);		
	}

	
	//Call with invalid arguments
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByIdIfVendorIdIsNull() {		
		service.findById(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByNameIfVendorNameIsNull() {
		service.findByName(null);		
	}
	

	@Test(expected=InvalidArgumentException.class)
	public void failMethodFindByTinIfVendorNameIsNull() {
		service.findByTin(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfVendorIsNull() {		
		service.create(null);		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfVendorIsNull() {		
		service.update(null);		
	}

	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodDeleteIfVendorIdIsNull() {
		service.delete(null);		
	}


	@Test(expected=InvalidArgumentException.class)
	public void failMethodCreateIfVendorHasNotNullId() {		
		service.create(newVendor(1L));		
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void failMethodUpdateIfVendorHasNullId() {		
		service.update(newVendor(null));		
	}

	
		
	//Entity not found
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByIdIfVendorIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.findById(1L);
	}

	

	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByNameIfVendorNameIsNotExist() {		
		when(mockRepository.selectByName(anyString()))
			.thenReturn(null);		
		
		service.findByName(VENDOR_NAME);
	}


	@Test(expected = EntityNotFoundException.class)
	public void failMethodFindByNameIfVendorTinIsNotExist() {		
		when(mockRepository.selectByTin(anyString()))
			.thenReturn(null);		
		
		service.findByTin(VENDOR_TIN);
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodUpdateIfVendordIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.update(newVendor(1L));
	}
	
	
	@Test(expected = EntityNotFoundException.class)
	public void failMethodDeleteIfVendordIdIsNotExist() {		
		when(mockRepository.selectById(anyLong()))
			.thenReturn(null);		
		
		service.delete(1L);		
	}
}
