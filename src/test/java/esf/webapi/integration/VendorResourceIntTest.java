package esf.webapi.integration;

import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.List;
import com.jayway.restassured.http.ContentType;

import esf.webapi.ApDeliveryItemResourceImpl;
import esf.webapi.VendorResourceImpl;
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
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import esf.repository.impl.VendorRepositoryImpl;
import esf.service.VendorService;
import esf.service.impl.VendorServiceImpl;

import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VendorResourceIntTest extends AbstractResourceTest {
	private DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_ap_vendor.xml"));
	
	@BeforeClass
	public static void setUpClass() {
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/vendor/";
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
				bind(VendorServiceImpl.class).to(VendorService.class);
				bind(new ApDeliveryItemResourceImpl()).to(ApDeliveryItemResourceImpl.class);
			}
		});

		setResource(new VendorResourceImpl());
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
	public void theListVendorsMayBeFound() {
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
			body("[0].name", is(not(nullValue()))).
			body("[0].tin", is(not(nullValue()))).
			body("[0].address", is(not(nullValue())));
	}
	
	
	@Test
	public void existingVendorMayBeFoundById() {
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
			body("name", is(equalTo(VENDOR_NAME))).
			body("tin", is(equalTo(VENDOR_TIN))).
			body("address", is(equalTo(VENDOR_ADDRESS)));
	}


	@Test
	public void existingVendorMayBeFoundByName() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + VENDOR_NAME).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(VENDOR_NAME))).
			body("tin", is(equalTo(VENDOR_TIN))).
			body("address", is(equalTo(VENDOR_ADDRESS)));
	}

	
	@Test
	public void existingVendorMayBeFoundByTin() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + VENDOR_TIN).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(VENDOR_NAME))).
			body("tin", is(equalTo(VENDOR_TIN))).
			body("address", is(equalTo(VENDOR_ADDRESS)));
	}
	
	
	@Test
	public void newVendorMayBeCreated() {
		Vendor origVendor = newVendor(null);
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(not(nullValue()))).
			body("name", is(equalTo(VENDOR_NAME))).
			body("tin", is(equalTo(VENDOR_TIN))).
			body("address", is(equalTo(VENDOR_ADDRESS)));					
	}
	
	
	@Test
	public void existingVendorMayBeUpdated()  {
		Vendor origVendor = newVendor(1L);				
		origVendor.setName(VENDOR_NAME + " (нов)");
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
			put(origVendor.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(VENDOR_NAME + " (нов)"))).
			body("tin", is(equalTo(VENDOR_TIN))).
			body("address", is(equalTo(VENDOR_ADDRESS)));
	}	
	
	
	@Test
	public void existingVendorMayBeDeleted() {
		Vendor origVendor = newVendor(1L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origVendor.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
	}	


	
	//Fail cases - Validation Exception
	
	@Test
	public void failMethodCreateIfTinIsTooLong() {
		Vendor origVendor = newVendor(null);
		origVendor.setTin(VENDOR_TIN + "1");
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("validation-exception")));					
	}	

	
	@Test
	public void failMethodUpdateIfTinIsTooLong() {
		Vendor origVendor = newVendor(1L);
		origVendor.setTin(VENDOR_TIN + "1");
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
			put(origVendor.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("validation-exception")));					
	}	
	
	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfVendorIdIsNotNull() {		
		Vendor origVendor = newVendor(1L);
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
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
	public void failMethodUpdateIfVendorIdIsNull() {		
		Vendor origVendor = newVendor(null);
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
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
	public void failMethodFindByIdIfVendorIdIsNotExist() {
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
	public void failMethodFindByNameIfVendorNameIsNotExist() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + "Заказчик 4").
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));	
	}

	
	@Test
	public void failMethodFindByTinIfVendorTinIsNotExist() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + "999999999999").
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));	
	}


	@Test
	public void failMethodUpdateIfVendorIdIsNotExist() {
		Vendor origVendor = newVendor(4L);
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(vendorToJson(origVendor)).
			put(origVendor.getId().toString()).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));		
	}
	

	@Test
	public void failMethodDeleteIfVendorIdIsNotExist() {
		Vendor origVendor = newVendor(4L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origVendor.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));		
	}
}