package oeg.upm.eta.rest.rdfcatalog;

import java.util.List; 
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import oeg.upm.eta.rest.rdfcatalog.utils.RDFDownload;


@Api(value = "RDF Catalog Service")
@SwaggerDefinition(tags= {@Tag(name="RDF Catalog Service", description="Requesting catalog with available EsDBpedia datasets")})
@Path("/RDFCatalogService") 
public class RDFCatalogService {  
   RDFCatalogDao rdfDao = new RDFCatalogDao();  
   
   @ApiOperation(value = "Get RDF Catalog", notes = "Get RDF Catalog, returns list with available datasets", responseContainer = "json", response = List.class)
   @GET 
   @Path("/rdfcatalog") 
   @Produces(MediaType.APPLICATION_JSON) 
   public List<RDFDataset> getUsers(){ 
      return rdfDao.listAllDatasets(); 
   }  
}