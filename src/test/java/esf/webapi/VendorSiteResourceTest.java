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

import esf.webapi.VendorSiteResourceImpl;
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
import esf.entity.VendorSite;
import esf.service.VendorService;
import esf.service.VendorSiteService;
import static esf.helper.EntitiesHelper.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VendorSiteResourceTest extends AbstractResourceTest {
	VendorService vendorService = null;
	VendorSiteService service = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		RestAssured.baseURI = "http://localhost:2222/esf/webapi/";
        RestAssured.basePath = "/vendor/1/vendorSite/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	
	@Before
	public void setUp() throws Exception {
		vendorService = mock(VendorService.class);
        service = mock(VendorSiteService.class);
		setBinder(new AbstractBinder() {
			protected void configure() {
				bind(vendorService).to(VendorService.class);
				bind(service).to(VendorSiteService.class);
			}
		});

		clearResource();
		addResource(new VendorResourceImpl());
		addResource(new VendorSiteResourceImpl());
		start(Binding.Manual);
	}
	
	
	@After
	public void tearDown() throws Exception {
		stop();
	}
	
	
	//Success cases
	
	@Test
	public void theListVendorSitesMayBeFound() {
		when(service.findByVendorId(anyLong()))
			.thenReturn(Arrays.asList(newVendorSite(1L)));
		
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
	
		verify(service, times(1)).findByVendorId(anyLong());
	}
	
	
	@Test
	public void existingVendorSiteMayBeFoundById() {
		when(service.findById(1L))
			.thenReturn(newVendorSite(1L));	
		
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
	
		verify(service, times(1)).findById(1L);
	}

	
	@Test
	public void newVendorSiteMayBeCreated() {
		VendorSite origVendorSite = newVendorSite(null);
		when(service.create(origVendorSite))
			.thenReturn(newVendorSite(1L));	
		
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
		
		verify(service, times(1)).create(anyObject());
	}
	
	
	@Test
	public void existingVendorSiteMayBeUpdated()  {
		VendorSite origVendorSite = newVendorSite(1L);				
		when(service.update(anyObject()))
			.then(returnsFirstArg());
		
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
			and().statusCode(200).
			body("id", is(equalTo(1))).
			body("contractNum", is(equalTo(VENDOR_SITE_NUM))).
			body("contractDate", is(equalTo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(VENDOR_SITE_DATE)  ))).
			body("vendorId", is(equalTo(1)));
		
		verify(service, times(1)).update(anyObject());
	}	
	
	
	@Test
	public void existingVendorSiteMayBeDeleted() {
		VendorSite origVendorSite = newVendorSite(1L);		
		when(service.delete(anyLong()))
			.thenReturn(true);		

		given().
			//log().all().
			accept("application/json; charset=utf-8").
			contentType("application/json; charset=utf-8").
		when().			
			delete(origVendorSite.getId().toString()).
		then().
			//log().all().
			and().statusCode(204);
		
		verify(service, times(1)).delete(anyLong());
	}	



	//Fail cases - Entity not found Exception

	@Test
	public void failMethodFindByIdIfVendorSiteIdIsNotExist() {
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
	public void failMethodUpdateIfVendorSiteIdIsNotExist() {
		VendorSite origVendorSite = newVendorSite(1L);
		when(service.update(anyObject()))
			.thenThrow(new EntityNotFoundException(1L));		
		
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
		
		verify(service, times(1)).update(anyObject());
	}
	

	@Test
	public void failMethodDeleteIfVendorSiteIdIsNotExist() {
		VendorSite origVendorSite = newVendorSite(1L);		
		when(service.delete(anyLong()))
			.thenThrow(new EntityNotFoundException(1L));		

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
		
		verify(service, times(1)).delete(anyLong());
	}

	
	//Fail cases - Invalid Argument Exception
	
	@Test
	public void failMethodCreateIfVendorSiteIdIsNotNull() {		
		VendorSite origVendorSite = newVendorSite(1L);
		when(service.create(anyObject()))
			.thenThrow(new InvalidArgumentException(origVendorSite));	
		
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
		
		verify(service, times(1)).create(anyObject());
	}
		

	@Test
	public void failMethodUpdateIfVendorSiteIdIsNull() {		
		VendorSite origVendorSite = newVendorSite(null);		
		when(service.update(anyObject()))
			.thenThrow(new InvalidArgumentException(origVendorSite));	
		
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
		
		verify(service, times(1)).update(anyObject());
	}

	
	//Fail cases - Repository not found Exception
	
	@Test
	public void failMethodGetAllIfRepositoryIsNull() {		
		when(service.findByVendorId(anyLong()))
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
		VendorSite origVendorSite = newVendorSite(null);
		when(service.create(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
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
			and().statusCode(500).
			body("errCode", is(not(nullValue()))).
			body("errMsg",  is(not(nullValue()))).	
			body("errCode", is(equalTo("repository-not-found-exception")));
	}


	@Test
	public void failMethodUpdateIfRepositoryIsNull() {		
		VendorSite origVendorSite = newVendorSite(1L);
		when(service.update(anyObject()))
			.thenThrow(new RepositoryNotFoundException());						
		
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