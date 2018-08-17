package oeg.upm.eta.rest.rdfcatalog.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ServerFTPListener
 *
 */
@WebListener
public class ServerFTPListener implements ServletContextListener {
	
	//todo load from properties
	private static final int FTP_PORT = 2221;
	private static final String FTP_USER = "apieta";
	private static final String FTP_PASS = "ftp.eta2018!";
	private ServerFTP server;
	
    /**
     * Default constructor. 
     */
    public ServerFTPListener() {
    	this.server = new ServerFTP(FTP_PORT, FTP_USER, FTP_PASS);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	this.server.stopServer();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	this.server.startServer();
    }

	public static int getFtpPort() {
		return FTP_PORT;
	}

	public static String getFtpUser() {
		return FTP_USER;
	}

	public static String getFtpPass() {
		return FTP_PASS;
	}
	
}
