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

import esf.webapi.CompanyResourceImpl;
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
import esf.entity.Company;
import esf.service.CompanyService;
import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompanyResourceTest extends AbstractResourceTest {
	CompanyService service = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/company/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {
        service = mock(CompanyService.class);
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(service).to(CompanyService.class);
			}
		});

		setResource(new CompanyResourceImpl());
		start(Binding.Manual);
	}
	
	
	@After
	public void tearDown() throws Exception {
		stop();
	}
	
	
	//Success cases
	
	@Test
	public void theListCompaniesMayBeFound() {
		when(service.find(anyObject()))
			.thenReturn(Arrays.asList(newCompany(1L), newCompany(2L), newCompany(3L) ));
		
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
	public void existingCompanyMayBeFoundById() {
		when(service.findById(1L))
			.thenReturn(newCompany(1L));	
		
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
			body("name", is(equalTo(COMPANY_NAME))).
			body("tin", is(equalTo(COMPANY_TIN))).
			body("address", is(equalTo(COMPANY_ADDRESS)));
	
		verify(service, times(1)).findById(1L);
	}


	@Test
	public void existingCompanyMayBeFoundByName() {
		when(service.findByName(COMPANY_NAME))
			.thenReturn(newCompany(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + COMPANY_NAME).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(COMPANY_NAME))).
			body("tin", is(equalTo(COMPANY_TIN))).
			body("address", is(equalTo(COMPANY_ADDRESS)));
		
		verify(service, times(1)).findByName(COMPANY_NAME);
	}

	
	@Test
	public void existingCompanyMayBeFoundByTin() {
		when(service.findByTin(COMPANY_TIN))
			.thenReturn(newCompany(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + COMPANY_TIN).
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(COMPANY_NAME))).
			body("tin", is(equalTo(COMPANY_TIN))).
			body("address", is(equalTo(COMPANY_ADDRESS)));
	
		verify(service, times(1)).findByTin(COMPANY_TIN);
	}
	
	
	@Test
	public void newCompanyMayBeCreated() {
		Company origCompany = newCompany(null);
		when(service.create(origCompany))
			.thenReturn(newCompany(1L));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
			post().
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(not(nullValue()))).
			body("name", is(equalTo(COMPANY_NAME))).
			body("tin", is(equalTo(COMPANY_TIN))).
			body("address", is(equalTo(COMPANY_ADDRESS)));					
	
		verify(service, times(1)).create(anyObject());
	}
	
	
	@Test
	public void existingCompanyMayBeUpdated()  {
		Company origCompany = newCompany(1L);				
		when(service.update(anyObject()))
			.then(returnsFirstArg());
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
			put(origCompany.getId().toString()).
		then().
			//log().all().
	        contentType(ContentType.JSON).
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("name", is(equalTo(COMPANY_NAME))).
			body("tin", is(equalTo(COMPANY_TIN))).
			body("address", is(equalTo(COMPANY_ADDRESS)));					
	
		verify(service, times(1)).update(anyObject());
	}	
	
	
	@Test
	public void existingCompanyMayBeDeleted() {
		Company origCompany = newCompany(1L);		
		when(service.delete(anyObject()))
			.thenReturn(true);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCompany.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
		
		verify(service, times(1)).delete(anyLong());
	}	



	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfCompanyIdIsNotExist() {
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
	public void failMethodFindByNameIfCompanyNameIsNotExist() {
		when(service.findByName(anyString()))
			.thenThrow(new EntityNotFoundException(COMPANY_NAME));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + COMPANY_NAME).
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
	public void failMethodFindByTinIfCompanyTinIsNotExist() {
		when(service.findByTin(anyString()))
			.thenThrow(new EntityNotFoundException(COMPANY_TIN));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byTin/" + COMPANY_TIN).
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
	public void failMethodUpdateIfCompanyIdIsNotExist() {
		Company origCompany = newCompany(1L);
		when(service.update(anyObject()))
			.thenThrow(new EntityNotFoundException(1L));		
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
			put(origCompany.getId().toString()).
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
	public void failMethodDeleteIfCompanyIdIsNotExist() {
		Company origCompany = newCompany(1L);		
		when(service.delete(anyLong()))
			.thenThrow(new EntityNotFoundException(1L));		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCompany.getId().toString()).
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
	public void failMethodCreateIfCompanyIdIsNotNull() {		
		Company origCompany = newCompany(1L);
		when(service.create(anyObject()))
			.thenThrow(new InvalidArgumentException(origCompany));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
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
	public void failMethodUpdateIfCompanyIdIsNull() {		
		Company origCompany = newCompany(null);		
		when(service.update(anyObject()))
			.thenThrow(new InvalidArgumentException(origCompany));	
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
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
			get("byName/" + COMPANY_NAME).
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
			get("byTin/" + COMPANY_TIN).
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
		Company origCompany = newCompany(null);
		when(service.create(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
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
		Company origCompany = newCompany(1L);
		when(service.update(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			body(companyToJson(origCompany)).
			put(origCompany.getId().toString()).
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