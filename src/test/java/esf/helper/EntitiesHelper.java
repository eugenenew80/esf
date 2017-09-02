package esf.helper;

//import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import esf.entity.*;
import esf.entity.invoice.ap.ApDeliveryItem;
import esf.entity.invoice.ar.ArDeliveryItem;

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
	public final static Date VENDOR_SITE_DATE=calendarFor(2017, 1, 1).getTime();
	
	public final static String CUSTOMER_SITE_NUM="001";
	public final static Date CUSTOMER_SITE_DATE=calendarFor(2017, 1, 1).getTime();

	
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

	public static ApDeliveryItem newApDeliveryItem() {
		ApDeliveryItem deliveryItem = new ApDeliveryItem();
		deliveryItem.setId(1l);
		deliveryItem.setContractNum(VENDOR_SITE_NUM);
		deliveryItem.setContractDate(VENDOR_SITE_DATE);
		deliveryItem.setVendor(newVendor(1L));
		return deliveryItem;
	}
	
	public static ApDeliveryItem newApDeliveryItem(Long id) {
		ApDeliveryItem deliveryItem = new ApDeliveryItem();
		deliveryItem.setId(id);
		return deliveryItem;
	}

	public static void assertApDeliveryItem(ApDeliveryItem deliveryItem) {
		assertNotNull(deliveryItem);
		assertNotNull(deliveryItem.getId());
		assertTrue(deliveryItem.getId()>0);
		assertEquals(VENDOR_SITE_NUM, deliveryItem.getContractNum());
		assertEquals(VENDOR_SITE_DATE, deliveryItem.getContractDate());
		assertVendor(deliveryItem.getVendor());
	}

	public static void assertApDeliveryItem(ApDeliveryItem deliveryItem1, ApDeliveryItem deliveryItem2) {
		assertNotNull(deliveryItem1);
		assertNotNull(deliveryItem1.getId());
		assertTrue(deliveryItem1.getId()>0);
		
		assertNotNull(deliveryItem2);
		assertNotNull(deliveryItem2.getId());
		assertTrue(deliveryItem2.getId()>0);
		
		assertEquals(deliveryItem1.getId(), deliveryItem2.getId());
		assertEquals(deliveryItem1.getContractNum(), deliveryItem2.getContractNum());
		assertEquals(deliveryItem1.getContractDate(), deliveryItem2.getContractDate());
		assertVendor(deliveryItem1.getVendor(), deliveryItem2.getVendor() );
	}

	public static String apDeliveryItemToJson(ApDeliveryItem deliveryItem) {
		JSONObject body = new JSONObject();
		
		try {
			if (deliveryItem.getId()!=null) 
				body.put("id", deliveryItem.getId());
				
			if (deliveryItem.getContractNum()!=null) 
				body.put("contractNum", deliveryItem.getContractNum());
			
			if (deliveryItem.getContractDate()!=null)
				body.put("contractDate", new SimpleDateFormat("yyyy-MM-dd").format(deliveryItem.getContractDate()));
			
			if (deliveryItem.getVendor()!=null)
				body.put("vendorId", deliveryItem.getVendor().getId());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	


	//CustomerSite

	public static ArDeliveryItem newArDeliveryItem() {
		ArDeliveryItem deliveryItem = new ArDeliveryItem();
		deliveryItem.setId(1l);
		deliveryItem.setContractNum(CUSTOMER_SITE_NUM);
		deliveryItem.setContractDate(CUSTOMER_SITE_DATE);
		deliveryItem.setCustomer(newCustomer(1L));
		return deliveryItem;
	}
	
	public static ArDeliveryItem newArDeliveryItem(Long id) {
		ArDeliveryItem deliveryItem = new ArDeliveryItem();
		deliveryItem.setId(id);
		return deliveryItem;
	}

	public static void assertArDeliveryItem(ArDeliveryItem deliveryItem) {
		assertNotNull(deliveryItem);
		assertNotNull(deliveryItem.getId());
		assertTrue(deliveryItem.getId()>0);
		assertEquals(CUSTOMER_SITE_NUM, deliveryItem.getContractNum());
		assertEquals(CUSTOMER_SITE_DATE, deliveryItem.getContractDate());
		assertCustomer(deliveryItem.getCustomer());
	}

	public static void assertArDeliveryItem(ArDeliveryItem deliveryItem1, ArDeliveryItem deliveryItem2) {
		assertNotNull(deliveryItem1);
		assertNotNull(deliveryItem1.getId());
		assertTrue(deliveryItem1.getId()>0);
		
		assertNotNull(deliveryItem2);
		assertNotNull(deliveryItem2.getId());
		assertTrue(deliveryItem2.getId()>0);
		
		assertEquals(deliveryItem1.getId(), deliveryItem2.getId());
		assertEquals(deliveryItem1.getContractNum(), deliveryItem2.getContractNum());
		assertEquals(deliveryItem1.getContractDate(), deliveryItem2.getContractDate());
		assertCustomer(deliveryItem1.getCustomer(), deliveryItem2.getCustomer() );
	}

	public static String arDeliveryItemToJson(ArDeliveryItem deliveryItem) {
		JSONObject body = new JSONObject();
		
		try {
			if (deliveryItem.getId()!=null) 
				body.put("id", deliveryItem.getId());
				
			if (deliveryItem.getContractNum()!=null) 
				body.put("contractNum", deliveryItem.getContractNum());
			
			if (deliveryItem.getContractDate()!=null)
				body.put("contractDate", new SimpleDateFormat("yyyy-MM-dd").format(deliveryItem.getContractDate()));
			
			if (deliveryItem.getCustomer()!=null)
				body.put("customerId", deliveryItem.getCustomer().getId());
		} 
		catch (JSONException e) {}
		
		return body.toString();
	}	

}
