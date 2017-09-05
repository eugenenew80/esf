package esf.webapi;

import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

import com.jayway.restassured.http.ContentType;

import esf.webapi.CustomerSiteResourceImpl;
import esf.webapi.helper.AbstractResourceTest;
import esf.webapi.helper.Binding;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import static com.jayway.restassured.RestAssured.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import com.jayway.restassured.RestAssured;

import esf.common.exception.EntityNotFoundException;
import esf.common.exception.InvalidArgumentException;
import esf.common.exception.RepositoryNotFoundException;
import esf.entity.CustomerSite;
import esf.service.CustomerService;
import esf.service.CustomerSiteService;
import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerSiteResourceTest extends AbstractResourceTest {
	CustomerService customerService = null;
	CustomerSiteService service = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/customer/1/customerSite/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {
		customerService = mock(CustomerService.class);
        service = mock(CustomerSiteService.class);
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(customerService).to(CustomerService.class);
				bind(service).to(CustomerSiteService.class);
			}
		});

		clearResource();
		addResource(new CustomerResourceImpl());
		addResource(new CustomerSiteResourceImpl());
		start(Binding.Manual);
	}
	
	
	@After
	public void tearDown() throws Exception {
		stop();
	}
	
	
	//Success cases
	
	@Test
	public void theListCustomerSitesMayBeFound() {
		when(service.findByCustomerId(anyLong()))
			.thenReturn(Arrays.asList(newCustomerSite(1L)));
		
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
	
		verify(service, times(1)).findByCustomerId(anyLong());
	}
	
	
	@Test
	public void existingCustomerSiteMayBeFoundById() {
		when(service.findById(1L))
			.thenReturn(newCustomerSite(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("1").
		then().
			log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("contractNum", is(equalTo(CUSTOMER_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(CUSTOMER_SITE_DATE)  ))).
			body("customerId", is(equalTo(1)));
	
		verify(service, times(1)).findById(1L);
	}

	
	@Test
	public void newCustomerSiteMayBeCreated() {
		CustomerSite origCustomerSite = newCustomerSite(null);
		when(service.create(origCustomerSite))
			.thenReturn(newCustomerSite(1L));	
		
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
		
		verify(service, times(1)).create(anyObject());
	}
	
	
	@Test
	public void existingCustomerSiteMayBeUpdated()  {
		CustomerSite origCustomerSite = newCustomerSite(1L);				
		when(service.update(anyObject()))
			.then(returnsFirstArg());
		
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
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("contractNum", is(equalTo(CUSTOMER_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(CUSTOMER_SITE_DATE)  ))).
			body("customerId", is(equalTo(1)));
		
		verify(service, times(1)).update(anyObject());
	}	
	
	
	@Test
	public void existingCustomerSiteMayBeDeleted() {
		CustomerSite origCustomerSite = newCustomerSite(1L);		
		when(service.delete(anyLong()))
			.thenReturn(true);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCustomerSite.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
		
		verify(service, times(1)).delete(anyLong());
	}	



	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfCustomerSiteIdIsNotExist() {
		when(service.findById(anyLong()))
			.thenThrow(new EntityNotFoundException(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("1").
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));
		
		verify(service, times(1)).findById(anyLong());
	}


	@Test
	public void failMethodUpdateIfCustomerSiteIdIsNotExist() {
		CustomerSite origCustomerSite = newCustomerSite(1L);
		when(service.update(anyObject()))
			.thenThrow(new EntityNotFoundException(1L));		
		
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
		
		verify(service, times(1)).update(anyObject());
	}
	

	@Test
	public void failMethodDeleteIfCustomerSiteIdIsNotExist() {
		CustomerSite origCustomerSite = newCustomerSite(1L);		
		when(service.delete(anyLong()))
			.thenThrow(new EntityNotFoundException(1L));		

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
		
		verify(service, times(1)).delete(anyLong());
	}

	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfCustomerSiteIdIsNotNull() {		
		CustomerSite origCustomerSite = newCustomerSite(1L);
		when(service.create(anyObject()))
			.thenThrow(new InvalidArgumentException(origCustomerSite));	
		
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
		
		verify(service, times(1)).create(anyObject());
	}
		

	@Test
	public void failMethodUpdateIfCustomerSiteIdIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(null);		
		when(service.update(anyObject()))
			.thenThrow(new InvalidArgumentException(origCustomerSite));	
		
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
		
		verify(service, times(1)).update(anyObject());
	}

	
	//Fail cases - Repository not found Exception
	
	@Test
	public void failMethodGetAllIfRepositoryIsNull() {		
		when(service.findByCustomerId(anyLong()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}
	

	@Test
	public void failMethodGetByIdIfRepositoryIsNull() {		
		when(service.findById(anyLong()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("1").
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}

	
	@Test
	public void failMethodCreateIfRepositoryIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(null);
		when(service.create(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
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
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}


	@Test
	public void failMethodUpdateIfRepositoryIsNull() {		
		CustomerSite origCustomerSite = newCustomerSite(1L);
		when(service.update(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
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
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}
	
	
	@Test
	public void failMethodDeleteIfRepositoryIsNull() {		
		when(service.delete(anyLong()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete("1").
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}
}