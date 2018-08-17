package oeg.upm.eta.rest.rdfcatalog;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import oeg.upm.eta.rest.rdfcatalog.utils.QualityMeasurement;
import oeg.upm.eta.rest.rdfcatalog.utils.ServerConfigListener;

public class RDFCatalogDao {
	
	
	public List<RDFDataset> listAllDatasets() {

		List<RDFDataset> rdfList = new ArrayList<RDFDataset>();

		// Create an empty model
		Model model = ModelFactory.createDefaultModel();
		model.read(ServerConfigListener.getProperty("datacatalogfile"));

		//model.write(System.out, "JSON-LD");
		
		// List all datasets in a catalog
		String queryString ="PREFIX dcat: <http://www.w3.org/ns/dcat#>";
		queryString +="PREFIX dc: <http://purl.org/dc/terms/>";
		queryString +="select ?catalog ?publisher ?dataset ?version ?issued ?lang ?lic ?desc where {";
		queryString +="   ?catalog a dcat:Catalog .";
		queryString +="   ?catalog dc:publisher ?publisher.";
		queryString +="   ?catalog dcat:dataset ?dataset .";
		queryString +="   ?dataset dc:hasVersion ?version .";
		queryString +="   ?dataset dc:issued ?issued .";
		queryString +="   ?dataset dc:language ?lang .";
		queryString +="   ?dataset dc:license ?lic .";
		queryString +="   ?dataset dc:description ?desc .";
		queryString +="}";

		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
		ResultSet results = qexec.execSelect() ;
		
		while (results.hasNext())
		{
			
			//get all the data about a dataset
			QuerySolution binding = results.nextSolution();
			Resource catalog = (Resource) binding.get("catalog");
			Resource publisher = (Resource) binding.get("publisher");
			Resource dataset = (Resource) binding.get("dataset");
			String version = binding.getLiteral("version").toString();
			String issued =  binding.getLiteral("issued").toString();
			String lang =  binding.getLiteral("lang").toString();
			String lic =  binding.get("lic").toString();
			String desc =  binding.getLiteral("desc").toString();
			
			String id = dataset.getURI().substring(dataset.getURI().lastIndexOf("/")+1);
			
			
			RDFDataset ds =  new RDFDataset(id, dataset.toString(), desc, version, issued, publisher.toString(), lang, lic);
			

			
			//Query quality data about dataset
			String queryStringQuality ="PREFIX dcat: <http://www.w3.org/ns/dcat#>\n";
			queryStringQuality +="PREFIX dc: <http://purl.org/dc/terms/>\n";
			queryStringQuality +="PREFIX dqv: <http://www.w3.org/ns/dqv#>\n";
			queryStringQuality +="PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n";
			queryStringQuality +="select ?value ?metric where {\n";
			queryStringQuality +="   <" + dataset.toString()+ "> dqv:hasQualityMeasurement ?qm .\n";
			queryStringQuality +="   ?qm dqv:value ?value .\n";
			queryStringQuality +="   ?qm dqv:isMeasurementOf ?mt .\n";
			queryStringQuality +="   ?mt skos:definition ?metric .\n";
			queryStringQuality +="}\n";
			
			System.out.println(queryStringQuality);

			
			Query queryQuality = QueryFactory.create(queryStringQuality);
			QueryExecution qexecQuality = QueryExecutionFactory.create(queryQuality, model) ;
			ResultSet resultsQuality = qexecQuality.execSelect() ;
			
			while (resultsQuality.hasNext())
			{
				QuerySolution bindingQuality = resultsQuality.nextSolution();
				Float value =  bindingQuality.getLiteral("value").getFloat();
				String metric = bindingQuality.getLiteral("metric").toString();
				
				
				QualityMeasurement qm = new QualityMeasurement(value, metric);
				ds.addQualityMeasurement(qm);
			}
			
			//Query the distribution of tha dataset
			//TODO so far we assume only on
			String queryStringSize ="PREFIX dcat: <http://www.w3.org/ns/dcat#>\n";
			queryStringSize +="PREFIX dc: <http://purl.org/dc/terms/>\n";
			queryStringSize +="PREFIX dqv: <http://www.w3.org/ns/dqv#>\n";
			queryStringSize +="PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n";
			queryStringSize +="select ?media ?bsize where {\n";
			queryStringSize +="   <" + dataset.toString()+ "> dcat:distribution ?distro .\n";
			queryStringSize +="   ?distro dcat:mediaType ?media .\n";
			queryStringSize +="   ?distro dcat:byteSize ?bsize .\n";
			queryStringSize +="}\n";
			
			System.out.println(queryStringSize);

			
			Query querySize = QueryFactory.create(queryStringSize);
			QueryExecution qexecSize = QueryExecutionFactory.create(querySize, model) ;
			ResultSet resultsSize = qexecSize.execSelect() ;
			
			if(resultsSize.hasNext())
			{
				QuerySolution bindingSize = resultsSize.nextSolution();
				String mediaType =  bindingSize.getLiteral("media").toString();
				int bsize = bindingSize.getLiteral("bsize").getInt();
				
				ds.setMediaType(mediaType);
				ds.setSize(bsize);
			}
			
			//add the dataset to the list
			System.out.println(ds.toString());
			rdfList.add(ds);

			
		}
		
		

		return rdfList;
	}
}
