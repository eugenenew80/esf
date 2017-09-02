package esf.webapi.integration;

import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.List;
import com.jayway.restassured.http.ContentType;
import esf.webapi.CustomerResourceImpl;
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
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import esf.repository.impl.CustomerRepositoryImpl;
import esf.service.CustomerService;
import esf.service.impl.CustomerServiceImpl;

import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerResourceIntTest extends AbstractResourceTest {
	private DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_customer.xml"));
	
	@BeforeClass
	public static void setUpClass() {
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/customer/";
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
				bind(CustomerServiceImpl.class).to(CustomerService.class);
			}
		});

		setResource(new CustomerResourceImpl());
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
	public void theListCustomersMayBeFound() {
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
	public void existingCustomerMayBeFoundById() {
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
	}


	@Test
	public void existingCustomerMayBeFoundByName() {
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
	}

	
	@Test
	public void existingCustomerMayBeFoundByTin() {
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
	}
	
	
	@Test
	public void newCustomerMayBeCreated() {
		Customer origCustomer = newCustomer(null);
		
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
	}
	
	
	@Test
	public void existingCustomerMayBeUpdated()  {
		Customer origCustomer = newCustomer(1L);				
		origCustomer.setName(CUSTOMER_NAME + " (нов)");
		
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
			body("name", is(equalTo(CUSTOMER_NAME + " (нов)"))).
			body("tin", is(equalTo(CUSTOMER_TIN))).
			body("address", is(equalTo(CUSTOMER_ADDRESS)));
	}	
	
	
	@Test
	public void existingCustomerMayBeDeleted() {
		Customer origCustomer = newCustomer(1L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCustomer.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
	}	


	
	//Fail cases - Validation Exception
	
	@Test
	public void failMethodCreateIfTinIsTooLong() {
		Customer origCustomer = newCustomer(null);
		origCustomer.setTin(CUSTOMER_TIN + "1");
		
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
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("validation-exception")));					
	}	

	
	@Test
	public void failMethodUpdateIfTinIsTooLong() {
		Customer origCustomer = newCustomer(1L);
		origCustomer.setTin(CUSTOMER_TIN + "1");
		
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
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("validation-exception")));					
	}	
	
	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfCustomerIdIsNotNull() {		
		Customer origCustomer = newCustomer(1L);
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
	}
		

	@Test
	public void failMethodUpdateIfCustomerIdIsNull() {		
		Customer origCustomer = newCustomer(null);
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
	}
	
	
	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfCustomerIdIsNotExist() {
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
	public void failMethodFindByNameIfCustomerNameIsNotExist() {
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
	public void failMethodFindByTinIfCustomerTinIsNotExist() {
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
	public void failMethodUpdateIfCustomerIdIsNotExist() {
		Customer origCustomer = newCustomer(4L);
		
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
	}
	

	@Test
	public void failMethodDeleteIfCustomerIdIsNotExist() {
		Customer origCustomer = newCustomer(4L);		

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
	}
}