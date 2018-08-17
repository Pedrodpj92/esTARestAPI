package oeg.upm.eta.rest.rdfcatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import oeg.upm.eta.rest.rdfcatalog.utils.FTPCon;
import oeg.upm.eta.rest.rdfcatalog.utils.JenaFileMerger;
import oeg.upm.eta.rest.rdfcatalog.utils.RDFDownload;
import oeg.upm.eta.rest.rdfcatalog.utils.SQLStatus;
import oeg.upm.eta.rest.rdfcatalog.utils.ServerCleanup;
import oeg.upm.eta.rest.rdfcatalog.utils.ServerConfigListener;
import oeg.upm.eta.rest.rdfcatalog.utils.ServerFTPListener;
import oeg.upm.eta.rest.rdfcatalog.utils.StatusCodes;

//https://www.tutorialspoint.com/restful/restful_addressing.htm
public class RDFDao {


	public void downloadWithHttp(HttpServletRequest request, HttpServletResponse response, String id, long n_offset_bytes) throws IOException {
		//Por ahora request no se utiliza, pero para el futuro si en el caso de que se necesite para coger datos o realizar comprobaciones


		//Pasar a variable que se cargue por archivo de propiedades
		//String pathDownloads = "C:\\Users\\ppozo\\Documents\\OEG\\esTextAnalytics\\catalogs\\out\\";

		//String pathDownloads = System.getenv("API_FILES_SITE");

		String confPath = System.getenv("API_ETA_CONF");
		Properties prop_download = new Properties();
		InputStream input = new FileInputStream(confPath);

		// load a properties file
		prop_download.load(input);

		String pathDownloads = prop_download.getProperty("dataoutfile");


		// reads input file from an absolute path
		String filePath = pathDownloads+"/"+id+".ttl";
		File downloadFile = new File(filePath);
		FileInputStream inStream = new FileInputStream(downloadFile);

		OutputStream outStream = response.getOutputStream();


		//da un null pointer que de momento no se el motivo, lo quito
		//sigue funcionando bien, aunque podria ser util para algun control de errores
		// obtains ServletContext
		//ServletContext context = getServletContext();

		//force download, maybe should be disable?
		response.setHeader("Content-Disposition", "attachment; filename=\""+id+".ttl"+"\"");

		//String mimeType = context.getMimeType(filePath);

		/*
		if (mimeType == null) {        
			// "The Internet Media Type / MIME Type for Turtle is "text/turtle""
			mimeType = "text/turtle";
		}//source: https://www.w3.org/TeamSubmission/turtle/
		 */

		String mimeType = "text/turtle";
		System.out.println("MIME type: " + mimeType);
		// modifies response
		response.setContentType(mimeType);
		//response.setContentLength((int) (downloadFile.length()-n_offset_bytes));//when we implement an extra parameter about resume download, change this

		System.out.println("longitud del archivo: "+downloadFile.length());
		System.out.println("punto del offset: "+n_offset_bytes);


		// obtains response's output stream
		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		System.out.println("Starting download");



		inStream.skip(n_offset_bytes); //skip offset bytes. We may need check length file

		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}


		System.out.println("File downloaded: "+id);
		inStream.close();
		outStream.close(); 
	}



	public RDFDownload getRDF(String id) {

		System.out.println("Getting RDF file for id: " + id);

		//check if the file has been already created
		StatusCodes code = SQLStatus.getStatus(id);

		//the file is ready
		if(code.equals(StatusCodes.OK))
		{
			FTPCon fc = new FTPCon(ServerFTPListener.getFtpPort(),
					"/"+id+"."+JenaFileMerger.DATA_SERIALIZATION ,
					ServerFTPListener.getFtpUser(),
					ServerFTPListener.getFtpPass());
			//the file is being created for these elements
			return new RDFDownload(code, fc, "File OK", id);
		}

		return new RDFDownload(code, "Still working", id);
	}

	public RDFDownload generateRDF(String req) 
	{
		System.out.println("put rdf with ids: " + req);

		JSONParser parser = new JSONParser();
		JSONArray entries;
		try {
			entries = (JSONArray) parser.parse(req.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return new RDFDownload(StatusCodes.ERROR, "Error parsing input JSON " + req.toString(), "NO_ID");
		}


		// Create an empty model
		Model model = ModelFactory.createDefaultModel();
		model.read(ServerConfigListener.getProperty("datacatalogfile"));

		List<String> urls = new ArrayList<String>();

		for(Object entry : entries)
		{

			// List all datasets in a catalog
			String queryString ="PREFIX dcat: <http://www.w3.org/ns/dcat#>";
			queryString +="PREFIX dc: <http://purl.org/dc/terms/>";
			queryString +="select ?url where {";
			queryString +="   <"+ entry.toString() +"> dcat:distribution ?dist .";
			queryString +="   ?dist dcat:downloadURL ?url .";
			queryString +="}";

			System.out.println(queryString);


			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
			ResultSet results = qexec.execSelect() ;

			if(!results.hasNext())
			{
				return new RDFDownload(StatusCodes.ERROR, "No files found for URI " + entry, "NO_ID");
			}

			while (results.hasNext())
			{
				QuerySolution binding = results.nextSolution();
				Resource url = (Resource) binding.get("url");
				urls.add(url.toString());
			}
		}

		if(urls.isEmpty())
		{
			return new RDFDownload(StatusCodes.ERROR, "No files found to be merged", "NO_ID");
		}


		//generate a unique id
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new RDFDownload(StatusCodes.ERROR, "Error generating the ID for the request", "NO_ID");
		}
		String text = "";

		for (String u : urls)
		{
			text += u;
		}


		try {
			md.update(text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new RDFDownload(StatusCodes.ERROR, "Error generating the ID for the request when encoding", "NO_ID");
		} 

		//get as a 64 chars string
		byte[] digest = md.digest();
		String id = String.format("%064x", new java.math.BigInteger(1, digest));

		//check if the file has been already created
		StatusCodes exist = SQLStatus.getStatus(id);

		if(exist.equals(StatusCodes.CREATED) || exist.equals(StatusCodes.PROCESSING) || exist.equals(StatusCodes.OK))
		{
			//the file is being created for these elements
			return new RDFDownload(exist, "This file has been requested before", id);
		}

		//run the jena worker on the pool
		Runnable jfmWorker = new JenaFileMerger(id, urls);

		//insert record on the database
		SQLStatus.insertStatus(id, StatusCodes.CREATED, JenaFileMerger.DATA_SERIALIZATION );

		//execute the worker job
		ServerCleanup.execute(jfmWorker);

		//return the processing code
		RDFDownload rdfd = new RDFDownload(StatusCodes.CREATED, "Processing datasets: " + entries, id);

		System.out.println("Download code: " + rdfd.toString());


		return rdfd;
	}


	public RDFDownload deleteRDF(String id) {

		if(SQLStatus.delete(id))
		{
			//TODO remove file
			return new RDFDownload(StatusCodes.DELETED,"File has been deleted", id);
		}

		return new RDFDownload(StatusCodes.UNEXPECTED, "Unexpected error while trying to delete file", id);
	}
}
