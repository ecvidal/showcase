package sampleapp.domain;


/**
 * Object that stores a server configuration 
 */
public class Server
{
	public int id;

	public String name;

	public Server(){}

	public Server(int id, String name ){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}