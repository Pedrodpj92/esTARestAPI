package oeg.upm.eta.rest.rdfcatalog.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerCleanup implements ServletContextListener {
	
	private static ExecutorService executor;
	private static final int POOL_SIZE = 10;


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Shuting down executor pool");
		executor.shutdown();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Creating executor pool size="+POOL_SIZE);
		executor = Executors.newFixedThreadPool(POOL_SIZE);
	}

	public static void execute(Runnable jfmWorker) {
		executor.execute(jfmWorker);
	} 

}
