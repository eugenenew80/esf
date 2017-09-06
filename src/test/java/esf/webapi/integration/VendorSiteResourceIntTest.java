package esf.webapi.integration;

import static org.hamcrest.Matchers.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import com.jayway.restassured.http.ContentType;

import esf.webapi.VendorResourceImpl;
import esf.webapi.VendorSiteResourceImpl;
import esf.webapi.helper.AbstractResourceTest;
import esf.webapi.helper.Binding;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import static com.jayway.restassured.RestAssured.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import com.jayway.restassured.RestAssured;
import esf.common.repository.Repository;
import esf.entity.Vendor;
import esf.entity.VendorSite;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import esf.repository.impl.VendorRepositoryImpl;
import esf.repository.impl.VendorSiteRepositoryImpl;
import esf.service.VendorService;
import esf.service.VendorSiteService;
import esf.service.impl.VendorServiceImpl;
import esf.service.impl.VendorSiteServiceImpl;

import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VendorSiteResourceIntTest extends AbstractResourceTest {
	private DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_ap_vendor_site.xml"));
	
	@BeforeClass
	public static void setUpClass() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/vendor/1/vendorSite/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {		
		dbUnitHelper = new DBUnitHelper();               		
		dbUnitHelper.beginTransaction();
		dbUnitHelper.cleanAndInsert(dataSetList);
		
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(new VendorRepositoryImpl(dbUnitHelper.getEntityManager())).to(new TypeLiteral<Repository<Vendor>>() {});
				bind(new VendorSiteRepositoryImpl(dbUnitHelper.getEntityManager())).to(new TypeLiteral<Repository<VendorSite>>() {});				
				
				bind(VendorServiceImpl.class).to(VendorService.class);
				bind(VendorSiteServiceImpl.class).to(VendorSiteService.class);				
			}
		});

		clearResource();
		addResource(new VendorResourceImpl());
		addResource(new VendorSiteResourceImpl());
		start(Binding.Manual);
	}
	
	
	@After
	public void tearDown() throws Exception {
		dbUnitHelper.commitTransaction(true);
		dbUnitHelper.closeDatabase();
		stop();
	}
	
	
	//Success cases
	
	@Test
	public void theListVendorSitesMayBeFound() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get().
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("[0].id", is(not(nullValue()))).
			body("[0].contractNum", is(not(nullValue()))).
			body("[0].contractDate", is(not(nullValue()))).
			body("[0].vendorId", is(not(nullValue())));
	}
	
	
	@Test
	public void existingVendorSiteMayBeFoundById() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("1").
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("contractNum", is(equalTo(VENDOR_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(VENDOR_SITE_DATE)  ))).
			body("vendorId", is(equalTo(1)));
	}


	@Test
	public void newVendorSiteMayBeCreated() {
		VendorSite origVendorSite = newVendorSite(null);
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorSiteToJson(origVendorSite)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(not(nullValue()))).
			body("contractNum", is(equalTo(VENDOR_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(VENDOR_SITE_DATE)  ))).
			body("vendorId", is(equalTo(1)));	
	}
	
	
	@Test
	public void existingVendorSiteMayBeUpdated()  {
		VendorSite origVendorSite = newVendorSite(1L);				
		origVendorSite.setContractNum(VENDOR_SITE_NUM + " (нов)");
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorSiteToJson(origVendorSite)).
			put(origVendorSite.getId().toString()).
		then().
		body("id", is(equalTo(1))).
		body("contractNum", is(equalTo(VENDOR_SITE_NUM + " (нов)"))).
		body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(VENDOR_SITE_DATE)  ))).
		body("vendorId", is(equalTo(1)));
	}	
	
	
	@Test
	public void existingVendorSiteMayBeDeleted() {
		VendorSite origVendorSite = newVendorSite(1L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origVendorSite.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
	}	

	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfVendorSiteIdIsNotNull() {		
		VendorSite origVendorSite = newVendorSite(1L);
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorSiteToJson(origVendorSite)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(400).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("invalid-argument-exception")));
	}
		

	@Test
	public void failMethodUpdateIfVendorSiteIdIsNull() {		
		VendorSite origVendorSite = newVendorSite(null);
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorSiteToJson(origVendorSite)).
			put("1").
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(400).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("invalid-argument-exception")));
	}
	
	
	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfVendorSiteIdIsNotExist() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("4").
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));	
	}



	@Test
	public void failMethodUpdateIfVendorSiteIdIsNotExist() {
		VendorSite origVendorSite = newVendorSite(4L);
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorSiteToJson(origVendorSite)).
			put(origVendorSite.getId().toString()).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));		
	}
	

	@Test
	public void failMethodDeleteIfVendorSiteIdIsNotExist() {
		VendorSite origVendorSite = newVendorSite(4L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origVendorSite.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));		
	}
}