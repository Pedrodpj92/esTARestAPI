package oeg.upm.eta.rest.rdfcatalog.utils;

public class FTPCon {
	private int port;
	private String url;
	private String user;
	private String pass;
	

	public FTPCon(int port, String url) {
		super();
		this.port = port;
		this.url = url;
		this.user = "";
		this.pass = "";
	}
	

	
	public FTPCon(int port, String url, String user, String pass) {
		super();
		this.port = port;
		this.url = url;
		this.user = user;
		this.pass = pass;
	}



	public int getPort() {
		return port;
	}



	public String getUrl() {
		return url;
	}



	public String getUser() {
		return user;
	}



	public String getPass() {
		return pass;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public void setPass(String pass) {
		this.pass = pass;
	}



	@Override
	public String toString() {
		return "FTPCon [port=" + port + ", url=" + url + ", user=" + user + ", pass=" + pass + "]";
	}
}
