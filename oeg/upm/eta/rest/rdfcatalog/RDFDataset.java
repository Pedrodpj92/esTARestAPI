package oeg.upm.eta.rest.rdfcatalog;
/**
 * @author isantana
 *
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement;

import oeg.upm.eta.rest.rdfcatalog.utils.QualityMeasurement; 
@XmlRootElement(name = "RDFDataset") 
public class RDFDataset implements Serializable {  
   private static final long serialVersionUID = 12L; 

   private String dataid; 
   private String uri; 
   private String description; 
   private String version;
   private String issued;
   private String publisher;
   private List<QualityMeasurement> quality;
   private String language;
   private String licencse;
   private int size;  
   private String mediaType;
   
   public RDFDataset(){} 

	public RDFDataset(String dataid, String uri, String description, String version, 
					  String issued, String publisher, 
					  String language, String licencse) {
		super();
		this.dataid = dataid;
		this.uri = uri;
		this.description = description;
		this.version = version;
		this.issued = issued;
		this.publisher = publisher;
		this.language = language;
		this.licencse = licencse;
		this.quality = new ArrayList<QualityMeasurement>();
	}

	public RDFDataset(String dataid, String uri, String description, String version, 
					  String issued, String publisher, 
					  String language, String licencse, int size) {
		super();
		this.dataid = dataid;
		this.uri = uri;
		this.description = description;
		this.version = version;
		this.issued = issued;
		this.publisher = publisher;
		this.language = language;
		this.licencse = licencse;
		this.quality = new ArrayList<QualityMeasurement>();
		this.size = size;
	}

	public String getUri() {
		return uri;
	}

	@XmlElement 
	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	@XmlElement 
	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	@XmlElement 
	public void setVersion(String version) {
		this.version = version;
	}

	public String getIssued() {
		return issued;
	}

	@XmlElement 
	public void setIssued(String issued) {
		this.issued = issued;
	}

	public String getPublisher() {
		return publisher;
	}

	@XmlElement 
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public List<QualityMeasurement> getQuality() {
		return quality;
	}

	@XmlElement 
	public void setQuality(List<QualityMeasurement> quality) {
		this.quality = quality;
	}
	
	public void addQualityMeasurement(QualityMeasurement qm)
	{
		this.quality.add(qm);
	}

	public String getLanguage() {
		return language;
	}

	@XmlElement 
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLicencse() {
		return licencse;
	}

	@XmlElement 
	public void setLicencse(String licencse) {
		this.licencse = licencse;
	}

	public int getSize() {
		return size;
	}
	@XmlElement 
	public void setSize(int size) {
		this.size = size;
	}

	public String getMediaType() {
		return mediaType;
	}

	@XmlElement 
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Override
	public String toString() {
		
		String q ="quality=";
		
		for(QualityMeasurement qm : quality)
		{
			q +=  qm.toString() + ",";
		}
		
		q+="\n";
		
		return "RDFDataset [dataid=" + dataid + ",\n uri=" + uri + ",\n description=" + description + ",\n version=" + version + ",\n issued=" + issued
				+ ",\n publisher=" + publisher + ",\n language=" + language +  ",\n quality=" + q + ",\n licencse=" + licencse
				+ ",\n size=" + size + "]";
	}
   
 
} 
