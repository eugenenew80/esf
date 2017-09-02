package esf.repository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
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

import esf.repository.VendorRepository;
import esf.repository.impl.VendorRepositoryImpl;
import esf.common.repository.query.ConditionType;
import esf.common.repository.query.MyQueryParam;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.Vendor;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import static esf.helper.EntitiesHelper.*;


public class VendorRepositoryTest {
	private static VendorRepository repository;
	private static DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_ap_vendor.xml"));
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		dbUnitHelper = new DBUnitHelper(); 
		repository = new VendorRepositoryImpl(dbUnitHelper.getEntityManager());;
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
	public void theListVendorsMayBeSelected() throws Exception {	
		List<Vendor> vendors = repository.selectAll();
		
		assertThat(vendors, is(not(nullValue())));
		assertThat(vendors, is(not(empty())));
		assertThat(vendors, hasSize(3));
		
		assertThat(vendors.get(0), is(not(nullValue())));
		assertThat(vendors.get(1), is(not(nullValue())));
		assertThat(vendors.get(2), is(not(nullValue())));
		
		assertThat(vendors.get(0).getId(), is(equalTo(1L)));
		assertThat(vendors.get(1).getId(), is(equalTo(2L)));
		assertThat(vendors.get(2).getId(), is(equalTo(3L)));
		assertVendor(vendors.get(0));
	}
	
	
	@Test
	public void theListVendorsMayBeSelectedByQuery() {
		Query query = QueryImpl.builder()
			.setParameter("name", new MyQueryParam("name", "Поставщик 1%", ConditionType.LIKE))
			.build();		
		
		List<Vendor> vendors = repository.select(query);			
		assertThat(vendors, is(not(nullValue())));
		assertThat(vendors, is(not(empty())));
		assertThat(vendors, hasSize(1));
		assertThat(vendors.get(0), is(not(nullValue())));
		assertThat(vendors.get(0).getId(), is(equalTo(1L)));
		assertVendor(vendors.get(0));		
		
		
		query = QueryImpl.builder().build();					
		vendors = repository.select(query);			
		assertThat(vendors, is(not(nullValue())));
		assertThat(vendors, is(not(empty())));
		assertThat(vendors, hasSize(3));
	}	
	
	
	@Test
	public void existingVendorMayBeSelectedById() throws Exception {
		Vendor vendor = repository.selectById(1L);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);
		
		vendor = repository.selectById(4L);
		assertThat(vendor, is(nullValue()));		
	}
	
	
	@Test
	public void existingVendorMayBeSelectedByName() throws Exception {
		Vendor vendor = repository.selectByName(VENDOR_NAME);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);

		vendor = repository.selectByName("бла бла бла");
		assertThat(vendor, is(nullValue()));	
	}

	
	@Test
	public void existingVendorMayBeSelectedByTin() throws Exception {
		Vendor vendor = repository.selectByTin(VENDOR_TIN);
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);

		vendor = repository.selectByTin("999999999999");
		assertThat(vendor, is(nullValue()));		
	}
	
	
	@Test
	public void newVendorMayBeInserted() {
		Vendor origVendor = newVendor(null);
		
		Vendor vendor = repository.insert(origVendor);				
		assertThat(vendor.getId(), is(not(nullValue())));
		
		vendor = repository.selectById(vendor.getId());
		assertThat(vendor, is(not(nullValue())));
		assertVendor(vendor);		
	}	
	
	
	@Test
	public void existingVendorMayBeUpdated() {
		Vendor origVendor = repository.selectById(1L);;	
		origVendor.setName(origVendor.getName() + " (нов)");
		
		Vendor vendor = repository.update(origVendor);				
		assertThat(vendor, is(not(nullValue())));
		
		vendor = repository.selectById(vendor.getId());
		assertVendor(origVendor, vendor);
	}	
	
	
	@Test
	public void existingVendorMayBeDeleted() {
		boolean result = repository.delete(4L);				
		assertThat(result, is(equalTo(false)));
		 
		result = repository.delete(1L);				
		assertThat(result, is(equalTo(true)));
		
		Vendor vendor = repository.selectById(1L);
		assertThat(vendor, is(nullValue()));
	}	
} 
