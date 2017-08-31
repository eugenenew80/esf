package esf.helper;

//import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import esf.entity.*;

public final class EntitiesHelper {	
	public final static String COMPANY_NAME="Компания 1";
	public final static String COMPANY_TIN="111111111111";
	public final static String COMPANY_RNN="222222222222";
	public final static String COMPANY_ADDRESS="Проспект Дружбы, 1";
	
    public static Calendar calendarFor(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal;
    }
	
	public static Company newCompany() {
		Company company = new Company();
		company.setId(1l);
		company.setName(COMPANY_NAME);
		company.setRnn(COMPANY_RNN);
		company.setTin(COMPANY_TIN);
		company.setAddress(COMPANY_ADDRESS);
		
		return company;
	}
	
	public static Company newCompany(Long id) {
		Company company = newCompany();
		company.setId(id);
		return company;
	}
	
	
	public static void assertCompany(Company company) {
		assertNotNull(company);
		assertNotNull(company.getId());
		assertTrue(company.getId()>0);
		assertEquals(COMPANY_NAME, company.getName());
		assertEquals(COMPANY_RNN, company.getRnn() );
		assertEquals(COMPANY_TIN, company.getTin() );
		assertEquals(COMPANY_ADDRESS, company.getAddress() );
	}
	
	public static void assertCompany(Company company1, Company company2) {
		assertNotNull(company1);
		assertNotNull(company1.getId());
		assertTrue(company1.getId()>0);
		
		assertNotNull(company2);
		assertNotNull(company2.getId());
		assertTrue(company2.getId()>0);
		
		assertEquals(company1.getId(), company2.getId());
		assertEquals(company1.getName(), company2.getName());
		assertEquals(company1.getRnn(), company2.getRnn() );
		assertEquals(company1.getTin(), company2.getTin() );
		assertEquals(company1.getAddress(), company2.getAddress() );
	}
	
	
	public static String companyToJson(Company company) {
		JSONObject body = new JSONObject();
		
		try {
			if (company.getId()!=null) 
				body.put("id", company.getId());
				
			if (company.getName()!=null) 
				body.put("name", company.getName());
			
			if (company.getTin()!=null)
				body.put("tin", company.getTin());
			
			if (company.getAddress()!=null)
				body.put("address", company.getAddress());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	
}
