package oeg.upm.eta.rest.rdfcatalog.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerConfigListener  implements ServletContextListener {

	static ServerConfig sc;
	
	public ServerConfigListener()
	{
		String confPath = System.getenv("API_ETA_CONF");
		
		System.out.println("Configuration path file=" + confPath);

		sc = new ServerConfig(confPath);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static String getProperty(String key)
	{
		return sc.getProperty(key);
	}

}
