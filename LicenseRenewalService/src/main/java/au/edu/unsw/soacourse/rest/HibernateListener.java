package au.edu.unsw.soacourse.rest;

public class HibernateListener implements ServletContextListener {  
  
    public void contextInitialized(ServletContextEvent event) {  
        HibernateHelper.getSessionFactory(); // Just call the static initializer of that class      
    }  
  
    public void contextDestroyed(ServletContextEvent event) {  
    	HibernateHelper.getSessionFactory().close(); // Free all resources  
    }  
} 