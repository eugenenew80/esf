package esf.webapi;

import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import com.jayway.restassured.http.ContentType;

import esf.webapi.CustomerResourceImpl;
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
import esf.entity.Customer;
import esf.service.CustomerService;
import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerResourceTest extends AbstractResourceTest {
	CustomerService service = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/customer/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {
        service = mock(CustomerService.class);
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(service).to(CustomerService.class);
				bind(new CustomerSiteResourceImpl()).to(CustomerSiteResourceImpl.class);
			}
		});

		setResource(new CustomerResourceImpl());
		start(Binding.Manual);
	}
	
	
	@After
	public void tearDown() throws Exception {
		stop();
	}
	
	
	//Success cases
	
	@Test
	public void theListCustomersMayBeFound() {
		when(service.find(anyObject()))
			.thenReturn(Arrays.asList(newCustomer(1L), newCustomer(2L), newCustomer(3L) ));
		
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
	
		verify(service, times(1)).find(anyObject());
	}
	
	
	@Test
	public void existingCustomerMayBeFoundById() {
		when(service.findById(1L))
			.thenReturn(newCustomer(1L));	
		
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
			body("name", is(equalTo(CUSTOMER_NAME))).
			body("tin", is(equalTo(CUSTOMER_TIN))).
			body("address", is(equalTo(CUSTOMER_ADDRESS)));
	
		verify(service, times(1)).findById(1L);
	}


	@Test
	public void existingCustomerMayBeFoundByName() {
		when(service.findByName(CUSTOMER_NAME))
			.thenReturn(newCustomer(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + CUSTOMER_NAME).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(CUSTOMER_NAME))).
			body("tin", is(equalTo(CUSTOMER_TIN))).
			body("address", is(equalTo(CUSTOMER_ADDRESS)));
		
		verify(service, times(1)).findByName(CUSTOMER_NAME);
	}

	
	@Test
	public void existingCustomerMayBeFoundByTin() {
		when(service.findByTin(CUSTOMER_TIN))
			.thenReturn(newCustomer(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + CUSTOMER_TIN).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(CUSTOMER_NAME))).
			body("tin", is(equalTo(CUSTOMER_TIN))).
			body("address", is(equalTo(CUSTOMER_ADDRESS)));
	
		verify(service, times(1)).findByTin(CUSTOMER_TIN);
	}
	
	
	@Test
	public void newCustomerMayBeCreated() {
		Customer origCustomer = newCustomer(null);
		when(service.create(origCustomer))
			.thenReturn(newCustomer(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(not(nullValue()))).
			body("name", is(equalTo(CUSTOMER_NAME))).
			body("tin", is(equalTo(CUSTOMER_TIN))).
			body("address", is(equalTo(CUSTOMER_ADDRESS)));					
	
		verify(service, times(1)).create(anyObject());
	}
	
	
	@Test
	public void existingCustomerMayBeUpdated()  {
		Customer origCustomer = newCustomer(1L);				
		when(service.update(anyObject()))
			.then(returnsFirstArg());
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
			put(origCustomer.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(CUSTOMER_NAME))).
			body("tin", is(equalTo(CUSTOMER_TIN))).
			body("address", is(equalTo(CUSTOMER_ADDRESS)));					
	
		verify(service, times(1)).update(anyObject());
	}	
	
	
	@Test
	public void existingCustomerMayBeDeleted() {
		Customer origCustomer = newCustomer(1L);		
		when(service.delete(anyObject()))
			.thenReturn(true);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCustomer.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
		
		verify(service, times(1)).delete(anyLong());
	}	



	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfCustomerIdIsNotExist() {
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
	public void failMethodFindByNameIfCustomerNameIsNotExist() {
		when(service.findByName(anyString()))
			.thenThrow(new EntityNotFoundException(CUSTOMER_NAME));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + CUSTOMER_NAME).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));
		
		verify(service, times(1)).findByName(anyString());
	}

	
	@Test
	public void failMethodFindByTinIfCustomerTinIsNotExist() {
		when(service.findByTin(anyString()))
			.thenThrow(new EntityNotFoundException(CUSTOMER_TIN));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + CUSTOMER_TIN).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));
		
		verify(service, times(1)).findByTin(anyString());
	}


	@Test
	public void failMethodUpdateIfCustomerIdIsNotExist() {
		Customer origCustomer = newCustomer(1L);
		when(service.update(anyObject()))
			.thenThrow(new EntityNotFoundException(1L));		
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
			put(origCustomer.getId().toString()).
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
	public void failMethodDeleteIfCustomerIdIsNotExist() {
		Customer origCustomer = newCustomer(1L);		
		when(service.delete(anyLong()))
			.thenThrow(new EntityNotFoundException(1L));		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCustomer.getId().toString()).
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
	public void failMethodCreateIfCustomerIdIsNotNull() {		
		Customer origCustomer = newCustomer(1L);
		when(service.create(anyObject()))
			.thenThrow(new InvalidArgumentException(origCustomer));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
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
	public void failMethodUpdateIfCustomerIdIsNull() {		
		Customer origCustomer = newCustomer(null);		
		when(service.update(anyObject()))
			.thenThrow(new InvalidArgumentException(origCustomer));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
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
		when(service.find(anyObject()))
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
	public void failMethodGetByNameIfRepositoryIsNull() {		
		when(service.findByName(anyString()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + CUSTOMER_NAME).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}
	

	@Test
	public void failMethodGetByTinIfRepositoryIsNull() {		
		when(service.findByTin(anyString()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + CUSTOMER_TIN).
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
		Customer origCustomer = newCustomer(null);
		when(service.create(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
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
		Customer origCustomer = newCustomer(1L);
		when(service.update(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(customerToJson(origCustomer)).
			put(origCustomer.getId().toString()).
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