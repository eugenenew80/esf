package esf.helper;

//import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import esf.entity.*;

public final class EntitiesHelper {	 
	public final static String COMPANY_NAME="Компания 1";
	public final static String COMPANY_TIN="111111111111";
	public final static String COMPANY_RNN="222222222222";
	public final static String COMPANY_ADDRESS="Проспект Дружбы, 1";
	
	public final static String CUSTOMER_NAME="Заказчик 1";
	public final static String CUSTOMER_TIN="300000000001";
	public final static String CUSTOMER_RNN="400000000001";
	public final static String CUSTOMER_ADDRESS="Проспект Мира, 1";

	public final static String VENDOR_NAME="Поставщик 1";
	public final static String VENDOR_TIN="100000000001";
	public final static String VENDOR_RNN="200000000001";
	public final static String VENDOR_ADDRESS="Проспект Победы, 1";
	
	public final static String VENDOR_SITE_NUM="001";
	public final static Date VENDOR_SITE_DATE=calendarFor(2017, 0, 1).getTime();
	
	public final static String CUSTOMER_SITE_NUM="001";
	public final static Date CUSTOMER_SITE_DATE=calendarFor(2017, 0, 1).getTime();

	
	public static Calendar calendarFor(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
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



	//Customer

	public static Customer newCustomer() {
		Customer сustomer = new Customer();
		сustomer.setId(1l);
		сustomer.setName(CUSTOMER_NAME);
		сustomer.setRnn(CUSTOMER_RNN);
		сustomer.setTin(CUSTOMER_TIN);
		сustomer.setAddress(CUSTOMER_ADDRESS);
		
		return сustomer;
	}
	
	public static Customer newCustomer(Long id) {
		Customer customer = newCustomer();
		customer.setId(id);
		return customer;
	}
	
	
	public static void assertCustomer(Customer customer) {
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertTrue(customer.getId()>0);
		assertEquals(CUSTOMER_NAME, customer.getName());
		assertEquals(CUSTOMER_RNN, customer.getRnn() );
		assertEquals(CUSTOMER_TIN, customer.getTin() );
		assertEquals(CUSTOMER_ADDRESS, customer.getAddress() );
	}
	
	public static void assertCustomer(Customer customer1, Customer customer2) {
		assertNotNull(customer1);
		assertNotNull(customer1.getId());
		assertTrue(customer1.getId()>0);
		
		assertNotNull(customer2);
		assertNotNull(customer2.getId());
		assertTrue(customer2.getId()>0);
		
		assertEquals(customer1.getId(), customer2.getId());
		assertEquals(customer1.getName(), customer2.getName());
		assertEquals(customer1.getRnn(), customer2.getRnn() );
		assertEquals(customer1.getTin(), customer2.getTin() );
		assertEquals(customer1.getAddress(), customer2.getAddress() );
	}
	
	
	public static String customerToJson(Customer customer) {
		JSONObject body = new JSONObject();
		
		try {
			if (customer.getId()!=null) 
				body.put("id", customer.getId());
				
			if (customer.getName()!=null) 
				body.put("name", customer.getName());
			
			if (customer.getTin()!=null)
				body.put("tin", customer.getTin());
			
			if (customer.getAddress()!=null)
				body.put("address", customer.getAddress());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	



	//Vendor

	public static Vendor newVendor() {
		Vendor vendor = new Vendor();
		vendor.setId(1l);
		vendor.setName(VENDOR_NAME);
		vendor.setRnn(VENDOR_RNN);
		vendor.setTin(VENDOR_TIN);
		vendor.setAddress(VENDOR_ADDRESS);
		
		return vendor;
	}
	
	public static Vendor newVendor(Long id) {
		Vendor vendor = newVendor();
		vendor.setId(id);
		return vendor;
	}
	
	
	public static void assertVendor(Vendor vendor) {
		assertNotNull(vendor);
		assertNotNull(vendor.getId());
		assertTrue(vendor.getId()>0);
		assertEquals(VENDOR_NAME, vendor.getName());
		assertEquals(VENDOR_RNN, vendor.getRnn() );
		assertEquals(VENDOR_TIN, vendor.getTin() );
		assertEquals(VENDOR_ADDRESS, vendor.getAddress() );
	}
	
	public static void assertVendor(Vendor vendor1, Vendor vendor2) {
		assertNotNull(vendor1);
		assertNotNull(vendor1.getId());
		assertTrue(vendor1.getId()>0);
		
		assertNotNull(vendor2);
		assertNotNull(vendor2.getId());
		assertTrue(vendor2.getId()>0);
		
		assertEquals(vendor1.getId(), vendor2.getId());
		assertEquals(vendor1.getName(), vendor2.getName());
		assertEquals(vendor1.getRnn(), vendor2.getRnn() );
		assertEquals(vendor1.getTin(), vendor2.getTin() );
		assertEquals(vendor1.getAddress(), vendor2.getAddress() );
	}
	
	
	public static String vendorToJson(Vendor vendor) {
		JSONObject body = new JSONObject();
		
		try {
			if (vendor.getId()!=null) 
				body.put("id", vendor.getId());
				
			if (vendor.getName()!=null) 
				body.put("name", vendor.getName());
			
			if (vendor.getTin()!=null)
				body.put("tin", vendor.getTin());
			
			if (vendor.getAddress()!=null)
				body.put("address", vendor.getAddress());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	


	//VendorSite

	public static VendorSite newVendorSite() {
		VendorSite vendorSite = new VendorSite();
		vendorSite.setId(1l);
		vendorSite.setContractNum(VENDOR_SITE_NUM);
		vendorSite.setContractDate(VENDOR_SITE_DATE);
		vendorSite.setVendor(newVendor(1L));
		return vendorSite;
	}
	
	public static VendorSite newVendorSite(Long id) {
		VendorSite vendorSite = newVendorSite();
		vendorSite.setId(id);
		return vendorSite;
	}

	public static void assertVendorSite(VendorSite vendorSite) {
		assertNotNull(vendorSite);
		assertNotNull(vendorSite.getId());
		assertTrue(vendorSite.getId()>0);
		assertEquals(VENDOR_SITE_NUM, vendorSite.getContractNum());
		assertEquals(VENDOR_SITE_DATE, vendorSite.getContractDate());
		assertVendor(vendorSite.getVendor());
	}

	public static void assertVendorSite(VendorSite vendorSite1, VendorSite vendorSite2) {
		assertNotNull(vendorSite1);
		assertNotNull(vendorSite1.getId());
		assertTrue(vendorSite1.getId()>0);
		
		assertNotNull(vendorSite2);
		assertNotNull(vendorSite2.getId());
		assertTrue(vendorSite2.getId()>0);
		
		assertEquals(vendorSite1.getId(), vendorSite2.getId());
		assertEquals(vendorSite1.getContractNum(), vendorSite2.getContractNum());
		assertEquals(vendorSite1.getContractDate(), vendorSite2.getContractDate());
		assertVendor(vendorSite1.getVendor(), vendorSite2.getVendor() );
	}

	public static String vendorSiteToJson(VendorSite vendorSite) {
		JSONObject body = new JSONObject();
		
		try {
			if (vendorSite.getId()!=null) 
				body.put("id", vendorSite.getId());
				
			if (vendorSite.getContractNum()!=null) 
				body.put("contractNum", vendorSite.getContractNum());
			
			if (vendorSite.getContractDate()!=null)
				body.put("contractDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(vendorSite.getContractDate()));
			
			if (vendorSite.getVendor()!=null)
				body.put("vendorId", vendorSite.getVendor().getId());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	


	//CustomerSite

	public static CustomerSite newCustomerSite() {
		CustomerSite customerSite = new CustomerSite();
		customerSite.setId(1l);
		customerSite.setContractNum(CUSTOMER_SITE_NUM);
		customerSite.setContractDate(CUSTOMER_SITE_DATE);
		customerSite.setCustomer(newCustomer(1L));
		return customerSite;
	}
	
	public static CustomerSite newCustomerSite(Long id) {
		CustomerSite customerSite = newCustomerSite();
		customerSite.setId(id);
		return customerSite;
	}

	public static void assertCustomerSite(CustomerSite customerSite) {
		assertNotNull(customerSite);
		assertNotNull(customerSite.getId());
		assertTrue(customerSite.getId()>0);
		assertEquals(CUSTOMER_SITE_NUM, customerSite.getContractNum());
		assertEquals(CUSTOMER_SITE_DATE, customerSite.getContractDate());
		assertCustomer(customerSite.getCustomer());
	}

	public static void assertCustomerSite(CustomerSite customerSite1, CustomerSite customerSite2) {
		assertNotNull(customerSite1);
		assertNotNull(customerSite1.getId());
		assertTrue(customerSite1.getId()>0);
		
		assertNotNull(customerSite2);
		assertNotNull(customerSite2.getId());
		assertTrue(customerSite2.getId()>0);
		
		assertEquals(customerSite1.getId(), customerSite2.getId());
		assertEquals(customerSite1.getContractNum(), customerSite2.getContractNum());
		assertEquals(customerSite1.getContractDate(), customerSite2.getContractDate());
		assertCustomer(customerSite1.getCustomer(), customerSite2.getCustomer() );
	}

	public static String customerSiteToJson(CustomerSite customerSite) {
		JSONObject body = new JSONObject();
		
		try {
			if (customerSite.getId()!=null) 
				body.put("id", customerSite.getId());
				
			if (customerSite.getContractNum()!=null) 
				body.put("contractNum", customerSite.getContractNum());
			
			if (customerSite.getContractDate()!=null)
				body.put("contractDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(customerSite.getContractDate()));
			
			if (customerSite.getCustomer()!=null)
				body.put("customerId", customerSite.getCustomer().getId());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	

}
