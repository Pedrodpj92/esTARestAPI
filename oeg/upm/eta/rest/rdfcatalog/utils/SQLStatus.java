package oeg.upm.eta.rest.rdfcatalog.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLStatus {

	private final static String DATABASE_NAME = "status2";
	
	public static boolean insertStatus(String id, StatusCodes code, String serial) {
		// TODO keep track of the process in a DB
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(ServerConfigListener.getProperty("datasqlstats"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Opened database successfully");

		Statement stmtCreate = null;

		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("Opened database successfully");

			stmtCreate = c.createStatement();
			String sql = "CREATE TABLE  IF NOT EXISTS " + DATABASE_NAME + ""
					+ "(id             INTEGER PRIMARY KEY  AUTOINCREMENT    NOT NULL,"
					+ " dataid         TEXT     NOT NULL, " 
					+ " status         CHAR(20) NOT NULL, "
					+ " serial         CHAR(20))";
			

			System.out.println("Create query:\n" + sql);
			
			stmtCreate.executeUpdate(sql);
			stmtCreate.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Table created successfully");

		Statement stmtInsert = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmtInsert = c.createStatement();
			String sql = "INSERT INTO " + DATABASE_NAME + " (dataid, status, serial) " +
						"VALUES ('" + id + "', '" + code + "' ,'" + serial+ "');";
			

			System.out.println("Insert query:\n" + sql);

			stmtInsert.executeUpdate(sql);

			stmtInsert.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Records created successfully");

		return true;
	}



	public static boolean updateStatus(String id, StatusCodes code) {
		Connection c = null;
		
		Statement stmtUpdate = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(ServerConfigListener.getProperty("datasqlstats"));
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmtUpdate = c.createStatement();
			String sql = "UPDATE " + DATABASE_NAME + " SET status = '"+code+"' "
	                + "WHERE dataid = '"+id+"';";
			
			System.out.println("Update query:\n" + sql);

			stmtUpdate.executeUpdate(sql);

			stmtUpdate.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Records updated successfully");

		return true;
	}



	public static StatusCodes getStatus(String id) {
		
		Connection c = null;
		
		Statement stmtSelect = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(ServerConfigListener.getProperty("datasqlstats"));
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmtSelect = c.createStatement();
	        String sql = "SELECT status FROM "+ DATABASE_NAME + " WHERE dataid = '" + id + "';";
	        
			System.out.println("Select query:\n" + sql);

			ResultSet rs = stmtSelect.executeQuery(sql);
			
			if(rs.next())
			{
				String status = rs.getString("status");
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return StatusCodes.valueOf(status);
			}

			stmtSelect.close();
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("No record found");

		return StatusCodes.UNEXPECTED;
	}



	public static boolean delete(String id) {
		
		Connection c = null;
		
		Statement stmtDelete = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(ServerConfigListener.getProperty("datasqlstats"));
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmtDelete = c.createStatement();
	        String sql = "DELETE FROM "+ DATABASE_NAME + " WHERE dataid = '" + id + "';";
	        
			System.out.println("Delete query:\n" + sql);

			stmtDelete.executeUpdate(sql);

			stmtDelete.close();
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	
		return true;
	}

}
