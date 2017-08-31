package esf.webapi.integration;

import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.List;
import com.jayway.restassured.http.ContentType;
import esf.webapi.CompanyResourceImpl;
import esf.webapi.helper.AbstractResourceTest;
import esf.webapi.helper.Binding;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import static com.jayway.restassured.RestAssured.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import com.jayway.restassured.RestAssured;
import esf.common.repository.Repository;
import esf.entity.Company;
import esf.helper.DBUnitHelper;
import esf.helper.DataSetLoader;
import esf.repository.impl.CompanyRepositoryImpl;
import esf.service.CompanyService;
import esf.service.impl.CompanyServiceImpl;

import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompanyResourceTest extends AbstractResourceTest {
	private DBUnitHelper dbUnitHelper;
	private List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "xx_company.xml"));
	
	@BeforeClass
	public static void setUpClass() {
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/company/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {		
		dbUnitHelper = new DBUnitHelper();               		
		dbUnitHelper.beginTransaction();
		dbUnitHelper.cleanAndInsert(dataSetList);
		
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(new CompanyRepositoryImpl(dbUnitHelper.getEntityManager())).to(new TypeLiteral<Repository<Company>>() {});
				bind(CompanyServiceImpl.class).to(CompanyService.class);
			}
		});

		setResource(new CompanyResourceImpl());
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
	public void theListCompaniesMayBeFound() {
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
	public void existingCompanyMayBeFoundById() {
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
	}


	@Test
	public void existingCompanyMayBeFoundByName() {
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
	}

	
	@Test
	public void existingCompanyMayBeFoundByTin() {
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
	}
	
	
	@Test
	public void newCompanyMayBeCreated() {
		Company origCompany = newCompany(null);
		
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
	}
	
	
	@Test
	public void existingCompanyMayBeUpdated()  {
		Company origCompany = newCompany(1L);				
		origCompany.setName(COMPANY_NAME + " (нов)");
		
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
			body("name", is(equalTo(COMPANY_NAME + " (нов)"))).
			body("tin", is(equalTo(COMPANY_TIN))).
			body("address", is(equalTo(COMPANY_ADDRESS)));
	}	
	
	
	@Test
	public void existingCompanyMayBeDeleted() {
		Company origCompany = newCompany(1L);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origCompany.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
	}	


	
	//Fail cases - Validation Exception
	
	@Test
	public void failMethodCreateIfTinIsTooLong() {
		Company origCompany = newCompany(null);
		origCompany.setTin(COMPANY_TIN + "1");
		
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
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("validation-exception")));					
	}	

	
	@Test
	public void failMethodUpdateIfTinIsTooLong() {
		Company origCompany = newCompany(1L);
		origCompany.setTin(COMPANY_TIN + "1");
		
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
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("validation-exception")));					
	}	
	
	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfCompanyIdIsNotNull() {		
		Company origCompany = newCompany(1L);
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
	}
		

	@Test
	public void failMethodUpdateIfCompanyIdIsNull() {		
		Company origCompany = newCompany(null);
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
	}
	
	
	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfCompanyIdIsNotExist() {
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
	public void failMethodFindByNameIfCompanyNameIsNotExist() {
		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().
			get("byName/" + "Компания 4").
		then().
			//log().all().
            contentType(ContentType.JSON).
			and().statusCode(404).
			body("errCode", is(not(nullValue()))).
			body("errMsg", is(not(nullValue()))).
			body("errCode", is(equalTo("entity-not-found-exception")));	
	}

	
	@Test
	public void failMethodFindByTinIfCompanyTinIsNotExist() {
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
	public void failMethodUpdateIfCompanyIdIsNotExist() {
		Company origCompany = newCompany(4L);
		
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
	}
	

	@Test
	public void failMethodDeleteIfCompanyIdIsNotExist() {
		Company origCompany = newCompany(4L);		

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
	}
}