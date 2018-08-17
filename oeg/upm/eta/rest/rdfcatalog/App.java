package oeg.upm.eta.rest.rdfcatalog;

import java.util.ArrayList;
import java.util.List;

import oeg.upm.eta.rest.rdfcatalog.utils.SQLStatus;
import oeg.upm.eta.rest.rdfcatalog.utils.StatusCodes;

public class App {

	public static void main(String args[])
	{
//		RDFCatalogDao rdf = new RDFCatalogDao();
//		
//		rdf.listAllDatasets();
//		
//		RDFDao rdfdao = new RDFDao(10);
//		List<String> uris = new ArrayList<String>();
//		uris.add("0001");
//		uris.add("0002");
//		//rdfdao.generateRDF(uris);

		String dataid = "97fb6339e048669ebce986d0ab4dfc74f7d60e667c8130a3c57b2dcb761df7af";

		System.out.println(">>>"+SQLStatus.getStatus(dataid));
		
		
	}
}
