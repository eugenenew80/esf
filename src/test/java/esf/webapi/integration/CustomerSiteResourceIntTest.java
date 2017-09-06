package esf.webapi.integration;

import static org.hamcrest.Matchers.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import com.jayway.restassured.http.ContentType;

import esf.webapi.CustomerResourceImpl;
import esf.webapi.CustomerSiteResourceImpl;
import esf.webapi.helper.AbstractResourceTest;
import esf.webapi.helper.Binding;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import static com.jayway.restassured.RestAssured.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import com.jayway.restassured.RestAssured;
import esf.common.repository.Repository;
import esf.entity.Customer;
import esf.entity.CustomerSite;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import esf.repository.impl.CustomerRepositoryImpl;
import esf.repository.impl.CustomerSiteRepositoryImpl;
import esf.service.CustomerService;
import esf.service.CustomerSiteService;
import esf.service.impl.CustomerServiceImpl;
import esf.service.impl.CustomerSiteServiceImpl;

import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerSiteResourceIntTest extends AbstractResourceTest {
	private DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_customer_site.xml"));
	
	@BeforeClass
	public static void setUpClass() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/customer/1/customerSite/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {		
		dbUnitHelper = new DBUnitHelper();               		
		dbUnitHelper.beginTransaction();
		dbUnitHelper.cleanAndInsert(dataSetList);
		
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(new CustomerRepositoryImpl(dbUnitHelper.getEntityManager())).to(new TypeLiteral<Repository<Customer>>() {});
				bind(new CustomerSiteRepositoryImpl(dbUnitHelper.getEntityManager())).to(new TypeLiteral<Repository<CustomerSite>>() {});				
				
				bind(CustomerServiceImpl.class).to(CustomerService.class);
				bind(CustomerSiteServiceImpl.class).to(CustomerSiteService.class);				
			}
		});

		clearResource();
		addResource(new CustomerResourceImpl());
		addResource(new CustomerSiteResourceImpl());
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
	public void theListCustomerSitesMayBeFound() {
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
			body("[0].customerId", is(not(nullValue())));
	}
	
	
	@Test
	public void existingCustomerSiteMayBeFoundById() {
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
			body("contractNum", is(equalTo(CUSTOMER_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(CUSTOMER_SITE_DATE)  ))).
			body("customerId", is(equalTo(1)));
	}


	@Test
	public void newCustomerSiteMayBeCreated() {
		CustomerSite origCustomerSite = newCustomerSite(null);
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerSiteToJson(origCustomerSite)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(not(nullValue()))).
			body("contractNum", is(equalTo(CUSTOMER_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(CUSTOMER_SITE_DATE)  ))).
			body("customerId", is(equalTo(1)));	
	}
	
	
	@Test
	public void existingCustomerSiteMayBeUpdated()  {
		CustomerSite origCustomerSite = newCustomerSite(1L);				
		origCustomerSite.setContractNum(CUSTOMER_SITE_NUM + " (нов)");
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerSiteToJson(origCustomerSite)).
			put(origCustomerSite.getId().toString()).
		then().
		body("id", is(equalTo(1))).
		body("contractNum", is(equalTo(CUSTOMER_SITE_NUM + " (нов)"))).
		body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(CUSTOMER_SITE_DATE)  ))).
		body("customerId", is(equalTo(1)));
	}	
	
	
	@Test
	public void existingCustomerSiteMayBeDeleted() {
		CustomerSite origCustomerSite = newCustomerSite(1L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCustomerSite.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
	}	

	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfCustomerSiteIdIsNotNull() {		
		CustomerSite origCustomerSite = newCustomerSite(1L);
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerSiteToJson(origCustomerSite)).
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
	public void failMethodUpdateIfCustomerSiteIdIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(null);
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerSiteToJson(origCustomerSite)).
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
	public void failMethodFindByIdIfCustomerSiteIdIsNotExist() {
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
	public void failMethodUpdateIfCustomerSiteIdIsNotExist() {
		CustomerSite origCustomerSite = newCustomerSite(4L);
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerSiteToJson(origCustomerSite)).
			put(origCustomerSite.getId().toString()).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));		
	}
	

	@Test
	public void failMethodDeleteIfCustomerSiteIdIsNotExist() {
		CustomerSite origCustomerSite = newCustomerSite(4L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCustomerSite.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));		
	}
}