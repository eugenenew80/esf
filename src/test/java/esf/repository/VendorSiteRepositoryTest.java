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

import esf.repository.VendorSiteRepository;
import esf.repository.impl.VendorSiteRepositoryImpl;
import esf.common.repository.query.ConditionType;
import esf.common.repository.query.MyQueryParam;
import esf.common.repository.query.Query;
import esf.common.repository.query.QueryImpl;
import esf.entity.VendorSite;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import static esf.helper.EntitiesHelper.*;


public class VendorSiteRepositoryTest {
	private static VendorSiteRepository repository;
	private static DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_ap_vendor_site.xml"));
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		dbUnitHelper = new DBUnitHelper(); 
		repository = new VendorSiteRepositoryImpl(dbUnitHelper.getEntityManager());;
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
	public void theListVendorSitesMayBeSelected() throws Exception {	
		List<VendorSite> vendorSites = repository.selectAll();
		
		assertThat(vendorSites, is(not(nullValue())));
		assertThat(vendorSites, is(not(empty())));
		assertThat(vendorSites, hasSize(3));
		
		assertThat(vendorSites.get(0), is(not(nullValue())));
		assertThat(vendorSites.get(1), is(not(nullValue())));
		assertThat(vendorSites.get(2), is(not(nullValue())));
		
		assertThat(vendorSites.get(0).getId(), is(equalTo(1L)));
		assertThat(vendorSites.get(1).getId(), is(equalTo(2L)));
		assertThat(vendorSites.get(2).getId(), is(equalTo(3L)));
		assertVendorSite(vendorSites.get(0));
	}
	
	
	@Test
	public void theListVendorSitesMayBeSelectedByQuery() {
		Query query = QueryImpl.builder()
			.setParameter("vendor.id", new MyQueryParam("vendorId", 1L, ConditionType.EQUALS))
			.build();		
		
		List<VendorSite> vendorSites = repository.select(query);			
		assertThat(vendorSites, is(not(nullValue())));
		assertThat(vendorSites, is(not(empty())));
		assertThat(vendorSites, hasSize(1));
		assertThat(vendorSites.get(0), is(not(nullValue())));
		assertThat(vendorSites.get(0).getId(), is(equalTo(1L)));
		assertVendorSite(vendorSites.get(0));		
		
		
		query = QueryImpl.builder().build();					
		vendorSites = repository.select(query);			
		assertThat(vendorSites, is(not(nullValue())));
		assertThat(vendorSites, is(not(empty())));
		assertThat(vendorSites, hasSize(3));
	}	
	
	
	@Test
	public void existingVendorSiteMayBeSelectedById() throws Exception {
		VendorSite vendorSite = repository.selectById(1L);
		assertThat(vendorSite, is(not(nullValue())));
		assertVendorSite(vendorSite);
		
		vendorSite = repository.selectById(4L);
		assertThat(vendorSite, is(nullValue()));		
	}
	
		
	@Test
	public void newVendorSiteMayBeInserted() {
		VendorSite origVendorSite = newVendorSite(null);
		VendorSite vendorSite = repository.insert(origVendorSite);				
		assertThat(vendorSite.getId(), is(not(nullValue())));
		
		vendorSite = repository.selectById(vendorSite.getId());
		assertThat(vendorSite, is(not(nullValue())));
		assertVendorSite(vendorSite);		
	}	
	
	
	@Test
	public void existingVendorSiteMayBeUpdated() {
		VendorSite origVendorSite = repository.selectById(1L);;	
		origVendorSite.setContractNum(origVendorSite.getContractNum() + " (нов)");
		
		VendorSite vendorSite = repository.update(origVendorSite);				
		assertThat(vendorSite, is(not(nullValue())));
		
		vendorSite = repository.selectById(vendorSite.getId());
		assertVendorSite(origVendorSite, vendorSite);
	}	
	
	
	@Test
	public void existingVendorSiteMayBeDeleted() {
		boolean result = repository.delete(4L);				
		assertThat(result, is(equalTo(false)));
		 
		result = repository.delete(1L);				
		assertThat(result, is(equalTo(true)));
		
		VendorSite vendorSite = repository.selectById(1L);
		assertThat(vendorSite, is(nullValue()));
	}	
} 
