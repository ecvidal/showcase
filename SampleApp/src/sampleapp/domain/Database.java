package sampleapp.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database
{
	// mySQL queries
	private final String LOADSERVERS = "INSERT INTO SERVER(ID, NAME) VALUES(?,?)";
	private final String EDITSERVER = "UPDATE SERVER SET NAME=? WHERE ID=?";
	private final String DELETESERVER = "DELETE FROM SERVER WHERE ID = ?";
	private final String LISTSERVERS = "SELECT ID, NAME FROM SERVER";
	private final String COUNTSERVERS = "SELECT COUNT(NAME) AS NSERVERS FROM SERVER";
	
	// mySQL connection data
	private String username = "";
	private String password = "";
	private String connectionString = "";	
	
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	private Connection connection = null;	
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	private String getPassword(){
		return password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	
	
	/**
	 * Creates a mySQL connection 
	 * 
	 * @returns The created connection
	 * @throws Exception If the connection is not set correctly
	 *             
	 */
	public Connection createConnection() throws Exception{		 
		
		 //Register the JDBC driver for MySQL.
		 Class.forName("com.mysql.jdbc.Driver");
		 String url = this.getConnectionString();
		 connection = DriverManager.getConnection(url + "?user=" + this.getUsername() + "&password=" + this.getPassword());		 		 

		 return connection;
	}

	/**
	 * Loads the server list into the database 
	 * 
	 * @serverList An array list of server objects to insert into the database
	 * @return The result of the operation
	 * @throws Exception If an error occurs during the process
	 *             
	 */
	public int loadServer(List<Server> serverList) throws Exception{
		int result = 0;
			
		connection = createConnection();
		for (int i=0; i < serverList.size(); i++){
			Server server = serverList.get(i);
			stmt = connection.prepareStatement(this.LOADSERVERS);
			stmt.setInt(1, server.getId());
			stmt.setString(2, server.getName());
			result = stmt.executeUpdate();				
		}

		this.close(connection);
		return result;
	}

	/**
	 * Edits the server info in the database 
	 * 
	 * @server The server to be edited, with the current id and the new name
	 * @return The result of the operation
	 * @throws Exception If an error occurs during the process
	 *             
	 */
	public int editServer(Server server) throws Exception{
		
		connection = createConnection();
		stmt = connection.prepareStatement(this.EDITSERVER);
		stmt.setString(1, server.getName());
		stmt.setInt(2, server.getId());
		int result = stmt.executeUpdate();			
			 
		this.close(connection);
		return result;
	}

	/**
	 * Edits the server info in the database 
	 * 
	 * @returns An array list with the servers, to be listed by the view
	 * @throws Exception If an error occurs during the process
	 *             
	 */
	public List<Server> listServers() throws Exception{
		List<Server> serverList = new ArrayList<Server>();
		
		connection = createConnection();
		stmt = connection.prepareStatement(this.LISTSERVERS);
		rs = stmt.executeQuery();
		while (rs.next()){
			serverList.add(this.createServer(rs));
		}
		this.close(connection);
		return serverList;
	}

	/**
	 * Counts the number of servers registered in the database 
	 * 
	 * @returns The registered number of servers
	 * @throws Exception If an error occurs during the process
	 *             
	 */	
	public int countServers() throws Exception{
		int nServers = 0;
		
		connection = createConnection();
		stmt = connection.prepareStatement(this.COUNTSERVERS);
		rs = stmt.executeQuery();
		while (rs.next()){
			 nServers = rs.getInt(1);
		}
		this.close(connection);
		return nServers;
	}
	
	/**
	 * Deletes a server from the database 
	 * 
	 * @server Server object with the id to be deleted
	 * @return The result of the operation
	 * @throws Exception If an error occurs during the process
	 *             
	 */
	public int deleteServer(Server server) throws Exception{
		connection = createConnection();
		stmt = connection.prepareStatement(this.DELETESERVER);
		stmt.setInt(1, server.getId());
		int result = stmt.executeUpdate();
		this.close(connection);
		return result;
	}
	
	
	/**
	 * Creates a server object from the resultSet 
	 * 
	 * @rs Database resultset with the server data
	 * @returns The created Server object
	 * @throws SQLException If there's an error accessing the resultset            
	 */
	private Server createServer(ResultSet rs) throws SQLException {
		return new Server(rs.getInt(1), rs.getString(2));
	}


	/**
	 * Closes all open database objects 
	 * 
	 * @conn The connection to be closed
	 * 
	 */
	public void close(Connection conn) {
		try{
	      if (rs != null) {
	    	  rs.close();
	      }
	
	      if (stmt != null) {
	    	  stmt.close();
	      }
	
	      if (conn != null) {
	    	  conn.close();
	      }
		}catch (Exception e){ }
	}
	
}