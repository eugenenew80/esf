package esf.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import esf.entity.Company;
import esf.entity.Customer;
import esf.entity.Vendor;
import esf.entity.VendorSite;


public class DBUnitHelper {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;
	private Connection connection;

	public DBUnitHelper() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory("esf_test"); 
		em = entityManagerFactory.createEntityManager();
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/esf_test?user=postgres&password=1");
	}
	
	public EntityManager getEntityManager() {
		return em;
	}


	public void closeDatabase() throws Exception {
		if( connection != null) {
			connection.close();
			connection = null;
		}
		
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
	
	public void beginTransaction() {
		em.getTransaction().begin();
	}
	
	
	public void commitTransaction() {
		em.getTransaction().commit();
	}
	
	
	public void commitTransaction(boolean clearContext) {
		commitTransaction();
		if (clearContext ) { 
			em.clear();
			clearCache();
		}
	}


	public void insert(List<DataSetLoader> loaders) throws Exception {
		for (DataSetLoader loader : loaders)
			loader.cleanAndInsert(connection);
	
		clearCache();
	}

	public void delete(List<DataSetLoader> loaders) throws Exception {
		for (DataSetLoader loader : loaders)
			loader.deleteAll(connection);
	
		clearCache();
	}

	public void cleanAndInsert(List<DataSetLoader> loaders) throws Exception {
		for (DataSetLoader loader : loaders)
			loader.cleanAndInsert(connection);
	
		clearCache();
	}

	
	private void clearCache() {
		em.getEntityManagerFactory().getCache().evict(Company.class);
		em.getEntityManagerFactory().getCache().evict(Customer.class);
		em.getEntityManagerFactory().getCache().evict(Vendor.class);
		em.getEntityManagerFactory().getCache().evict(VendorSite.class);
	}
}
