package pos_java_maven_hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

	private static EntityManagerFactory factory = null;
	
	static {
		init();
	}
		
		public static void init() {
			try {

				if (factory == null) {
					factory = Persistence.createEntityManagerFactory("pos-java-maven-hibernate");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

		public static  EntityManager geEntityManager() {
			return factory.createEntityManager();
			
		}
		
		public static Object getPrimaryKey(Object entity) { //retorna a primary key
			return factory.getPersistenceUnitUtil().getIdentifier(entity);
		}
		
}
	

