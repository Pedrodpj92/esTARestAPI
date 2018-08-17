package oeg.upm.eta.rest.swagger;




import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import io.swagger.jaxrs.config.BeanConfig;



public class SwaggerConfigurationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		BeanConfig beanConfig = new BeanConfig();
		//beanConfig.setHost("localhost:8080");
		beanConfig.setHost("api.textanalytics.linkeddata.es");
		beanConfig.setSchemes(new String[] {"http"});
		beanConfig.setBasePath("/esTARestAPI/rest");
		beanConfig.setTitle("EsTextAnalytics REST API Documentation");
		beanConfig.setVersion("1.1");
		beanConfig.setResourcePackage("oeg.upm.eta.rest.rdfcatalog");
		beanConfig.setScan(true);
		beanConfig.setPrettyPrint(true);
	}
}
