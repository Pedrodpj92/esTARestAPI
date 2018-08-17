package oeg.upm.eta.rest.rdfcatalog.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ServerConfig {
	
	Properties prop;
	Logger log = LogManager.getLogger(ServerConfig.class);

	
	public ServerConfig(String path)
	{

		org.apache.log4j.BasicConfigurator.configure();

		
		prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(path);

			// load a properties file
			prop.load(input);

			
			// get the property value and print it out
			log.info("Loading properties");
			log.info("datacatalogfile"+prop.getProperty("datacatalogfile"));
			log.info("dataoutfile"+prop.getProperty("dataoutfile"));
			log.info("datasqlstats"+prop.getProperty("datasqlstats"));
			log.info("datasetbaseuri"+prop.getProperty("datasetbaseuri"));
			log.info("ftpusers"+prop.getProperty("ftpusers"));

		} catch (IOException ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getProperty(String key)
	{
		return prop.getProperty(key);
	}

	
	

}
