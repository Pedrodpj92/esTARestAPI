package oeg.upm.eta.rest.rdfcatalog.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

//https://www.journaldev.com/1069/threadpoolexecutor-java-thread-pool-example-executorservice
public class JenaFileMerger implements Runnable {

	private List<String> uris;
	private String id;
	public static String DATA_SERIALIZATION = "ttl";

	public JenaFileMerger(String id, List<String> uris) {
		super();
		this.id = id;
		this.uris = uris;
	}

	@Override
	public void run() {

		//at this point the server has already sent a CREATED code
		//and the status on th DB is CREATED, time to start PROCESSING
		
		System.out.println("Running JFM worker for: " + id);

		
		if(SQLStatus.updateStatus(id, StatusCodes.PROCESSING))
		{
			System.out.println("New status: " + StatusCodes.PROCESSING);
		}
		
		System.out.println("JFM Working for: " + id);

		// Create an empty model
		Model model = ModelFactory.createDefaultModel();


		for (String u : uris) {
			model.read(u);
		}

		System.out.println("JFM Model:");

		// Write it to standard out
		//model.write(System.out);

		FileWriter out;
		String outFilePath = "OUT_FILE_PATH_VAR";
		try {
			outFilePath = ServerConfigListener.getProperty("dataoutfile") + id + "." + DATA_SERIALIZATION;
			out = new FileWriter(outFilePath);
			model.write(out, "TTL");
		} catch (IOException e) {
			e.printStackTrace();
			if(SQLStatus.updateStatus(id, StatusCodes.ERROR))
			{
				System.out.println("New status: " + StatusCodes.ERROR);
				return;
			}
		}
		
		//check is the file actually exists
		File f = new File(outFilePath);
		if(!f.exists() || f.isDirectory()) { 
			System.out.println("We could not check that the file indeed exists at " + outFilePath);
			if(SQLStatus.updateStatus(id, StatusCodes.ERROR))
			{
				System.out.println("New status: " + StatusCodes.ERROR);
				return;
			}
		}


		if(SQLStatus.updateStatus(id, StatusCodes.OK))
		{
			System.out.println("New status: " + StatusCodes.OK);
			System.out.println("JFM DONE! saved at " + ServerConfigListener.getProperty("dataoutfile") + id + ".ttl");
		}
		else
		{
			System.out.println("JFM DONE! aimed to save at " + ServerConfigListener.getProperty("dataoutfile") + id + ".ttl  but the status could not be updated.");
		}


	}

}
