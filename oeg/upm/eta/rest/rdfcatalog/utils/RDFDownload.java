package oeg.upm.eta.rest.rdfcatalog.utils;

public class RDFDownload {
	
	private StatusCodes status;
	private FTPCon connection;
	private String message;
	private String dataid;
	

	public RDFDownload(StatusCodes status,  String message, String id) {
		super();
		this.status = status;
		this.connection = new FTPCon(-1,"","","");
		this.message = message;
		this.dataid = id;
	}
	
	public RDFDownload(StatusCodes status, FTPCon connection, String message, String id) {
		super();
		this.status = status;
		this.connection = connection;
		this.message = message;
		this.dataid = id;
	}
	
	public StatusCodes getStatus() {
		return status;
	}
	
	public FTPCon getConnection() {
		return connection;
	}
	
	public void setStatus(StatusCodes status) {
		this.status = status;
	}
	
	public void setConnection(FTPCon connection) {
		this.connection = connection;
	}
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	@Override
	public String toString() {
		return "RDFDownload [status=" + status + ", connection=" + connection + ", message=" + message + ", dataid=" + dataid
				+ "]";
	}



}
