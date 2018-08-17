package oeg.upm.eta.rest.rdfcatalog;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPSClient.html

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import oeg.upm.eta.rest.rdfcatalog.utils.RDFDownload;
import oeg.upm.eta.rest.rdfcatalog.utils.StatusCodes;  

@Api(value = "RDF Service")
@SwaggerDefinition(tags= {@Tag(name="RDF Service", description="API Operations to work with available catalog EsDBpedia datasets")})
@Path("/RDFService") 
public class RDFService extends HttpServlet{  
	
	RDFDao rdfDao = new RDFDao();  

	private static final long serialVersionUID = 1L;

	@ApiOperation(value = "download RDF via http", notes = "download RDF via http, starting from a particular byte", response = void.class)
	@GET 
	@Path("/rdf/downloadhttp/{id}/resume/{n_offset_bytes}") 
	//@Produces(MediaType.APPLICATION_JSON) //this one not produce JSON
	public void doGet(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@ApiParam(value = "Id referenced to RDF requested previosly", required = true) @PathParam("id") String id,
			@ApiParam(value = "offset to request RDF and its byte where should continue", required = true) @PathParam("n_offset_bytes") long n_offset_bytes)
			throws IOException, ServletException { 

		RDFDownload dataDownload = rdfDao.getRDF(id);

		if(dataDownload.getStatus().equals(StatusCodes.OK)) {
			
			rdfDao.downloadWithHttp(request, response, id, n_offset_bytes);
			
		}
		else {
	        System.out.println("File not ready: "+id);
	        System.out.println("redirecting...");
			response.sendRedirect("/esTARestAPI/rest/RDFService/rdf/"+id);
		}
	} 

	
	
	
	@ApiOperation(value = "Get RDF data by id", notes = "Get RDF data by id, returns operation response in json format", responseContainer = "json", response = RDFDownload.class)
	@GET 
	@Path("/rdf/{id}") 
	@Produces(MediaType.APPLICATION_JSON) 
	public RDFDownload getRdf(@ApiParam(value = "Id referenced to RDF requested", required = true) @PathParam("id") String id){ 
		return rdfDao.getRDF(id); 
	} 

	@ApiOperation(value = "Generate RDF", notes = "Generate RDF following available catalog datasets, returns operation response in json format", responseContainer = "json", response = RDFDownload.class)
	@PUT 
	@Path("/rdf/") 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RDFDownload putRdf(@ApiParam(value = "String in json format requesting what rdf from catalog should be merged ", required = true) String req){ 
		return rdfDao.generateRDF(req);
	}  

	@ApiOperation(value = "Delete RDF data by id", notes = "Delete RDF data by id, returns operation response in json format", responseContainer = "json", response = RDFDownload.class)
	@DELETE 
	@Path("/rdf/{id}") 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RDFDownload deleteRdf(@ApiParam(value = "Id referenced to RDF requested to be deleted", required = true) @PathParam("id") String id){ 
		return rdfDao.deleteRDF(id); 
	}   
}