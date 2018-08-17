package oeg.upm.eta.rest.rdfcatalog.utils;

public class QualityMeasurement {
	private float value;
	private String metric;

	public QualityMeasurement(float value, String metric) {
		super();
		this.value = value;
		this.metric = metric;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	@Override
	public String toString() {
		return "QualityMeasurement [value=" + value + ", metric=" + metric + "]";
	}
	
}

/*
 * 
 * 
 * 
 *     "@id" : "http://estextanalytics.linkeddata.es/qm/0001/02",
    "@type" : "dqv:QualityMeasurement",
    "isMeasurementOf" : "http://estextanalytics.linkeddata.es/metric/resource_precision",
    "dqv:value" : 0.94

{
    "@id" : "http://estextanalytics.linkeddata.es/metric/resource_precision",
    "@type" : "dqv:Metric",
    "definition" : {
      "@language" : "es",
      "@value" : "Precision media contada en base al total de elementos a adivinar para un recurso frente a los adivinados para dicho recurso"
    },
 */