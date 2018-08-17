package oeg.upm.eta.rest.rdfcatalog.utils;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;

//download: http://www.codejava.net/java-se/networking/ftp/java-ftp-file-download-tutorial-and-example
//users: https://www.programcreek.com/java-api-examples/index.php?api=org.apache.ftpserver.FtpServer
public class ServerFTP {
	
	private FtpServer server;
	private String user;
	private String pass;
	
	public ServerFTP(int port, String user, String pass)
	{
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		// set the port of the listener
		factory.setPort(port);
		
		this.user = user;
		this.pass = pass;
		
		// replace the default listener
		serverFactory.addListener("default", factory.createListener());
		
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File(ServerConfigListener.getProperty("ftpusers")));
		userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
		UserManager um = userManagerFactory.createUserManager();
		BaseUser buser = new BaseUser();
		buser.setName(this.user);
		buser.setPassword(this.pass);
		buser.setHomeDirectory(ServerConfigListener.getProperty("dataoutfile"));
		try {
			um.save(buser);
		} catch (FtpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serverFactory.setUserManager(um);

		
		// start the server
		this.server = serverFactory.createServer(); 
		
		System.out.println("Server running in port:" + port);
		
		
		
	}
	
	public void startServer()
	{
		try {
			this.server.start();
		} catch (FtpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void stopServer()
	{
		this.server.stop();
	}
	
}

