package services.data;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
/**
 * 
* <p>Title: HibernateUtils</p>
* <p>Description: Class for Hibernate session management </p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class HibernateUtils {
	
private static final SessionFactory sessionFactory;
	
	
	static{
		
			
			Configuration config = new Configuration();
			
			config.configure();
			
			StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder().applySettings(config.getProperties());
			
			ServiceRegistry sr = srb.build();
			
			sessionFactory  = config.buildSessionFactory(sr);
		
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
}


