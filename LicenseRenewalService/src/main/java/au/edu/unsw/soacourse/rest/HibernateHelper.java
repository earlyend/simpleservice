package au.edu.unsw.soacourse.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
public class HibernateHelper
{
	private static final SessionFactory sessionFactory = create_sessionFactory();
	
	@PostConstruct
	public Configuration Configuration_method()
	{
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		return cfg;
	}
	
	@PostConstruct
	private static SessionFactory create_sessionFactory()
	{
		 return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@PreDestroy
	public static void destroy(){
		getSessionFactory().close();
	}
}